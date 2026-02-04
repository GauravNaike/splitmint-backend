package com.karbon.splitmint.service;

import org.springframework.stereotype.Service;

import com.karbon.splitmint.model.Expense;
import com.karbon.splitmint.model.Group;
import com.karbon.splitmint.model.Participant;
import com.karbon.splitmint.repository.ExpenseRepository;
import com.karbon.splitmint.repository.GroupRepository;
import com.karbon.splitmint.repository.ParticipantRepository;

import lombok.RequiredArgsConstructor;

import com.karbon.splitmint.dto.BalanceDto;
import com.karbon.splitmint.dto.SettlementDto;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final ParticipantRepository participantRepository;
    private final ExpenseRepository expenseRepository;

    public Group createGroup(String name) {
        Group group = new Group(name);
        return groupRepository.save(group);
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public Group getGroupById(String groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
    }

    @org.springframework.transaction.annotation.Transactional
    public Participant addParticipant(String groupId, String name) {
        Group group = getGroupById(groupId);
        Participant participant = new Participant(name);
        participant.setGroup(group);
        return participantRepository.save(participant);
    }

    @org.springframework.transaction.annotation.Transactional
    public Expense addExpense(
            String groupId,
            String description,
            double amount,
            String paidBy,
            List<String> participantIds) {

        Group group = getGroupById(groupId);

        // Validate participants
        Set<String> groupParticipantIds = group.getParticipants().stream()
                .map(Participant::getId)
                .collect(Collectors.toSet());

        if (!groupParticipantIds.contains(paidBy)) {
            throw new IllegalArgumentException("Payer (ID: " + paidBy + ") is not a participant of this group.");
        }

        for (String pid : participantIds) {
            if (!groupParticipantIds.contains(pid)) {
                throw new IllegalArgumentException(
                        "Participant (ID: " + pid + ") is not a participant of this group.");
            }
        }

        Expense expense = new Expense(
                description,
                amount,
                paidBy,
                participantIds);

        expense.setGroup(group);
        return expenseRepository.save(expense);
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<BalanceDto> calculateBalances(String groupId) {

        Group group = getGroupById(groupId);

        Map<String, Double> balanceMap = new HashMap<>();

        // Initialize balances
        for (Participant p : group.getParticipants()) {
            balanceMap.put(p.getId(), 0.0);
        }

        for (Expense expense : group.getExpenses()) {

            List<String> involved = expense.getParticipantIds();
            if (involved == null || involved.isEmpty())
                continue;

            double share = expense.getAmount() / involved.size();

            // Everyone pays their share
            for (String participantId : involved) {
                if (!balanceMap.containsKey(participantId))
                    continue;

                balanceMap.put(
                        participantId,
                        balanceMap.get(participantId) - share);
            }

            // Payer gets full credit
            String payerId = expense.getPaidByParticipantId();
            if (payerId != null && balanceMap.containsKey(payerId)) {
                balanceMap.put(
                        payerId,
                        balanceMap.get(payerId) + expense.getAmount());
            }
        }

        return balanceMap.entrySet()
                .stream()
                .map(e -> new BalanceDto(e.getKey(), round(e.getValue())))
                .collect(Collectors.toList());
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<SettlementDto> calculateSettlements(String groupId) {

        List<BalanceDto> balances = calculateBalances(groupId);

        Map<String, Double> net = new HashMap<>();
        for (BalanceDto b : balances) {
            net.put(b.getParticipantId(), b.getAmount());
        }

        List<SettlementDto> settlements = new ArrayList<>();

        List<Map.Entry<String, Double>> debtors = net.entrySet()
                .stream()
                .filter(e -> e.getValue() < 0)
                .collect(Collectors.toList());

        List<Map.Entry<String, Double>> creditors = net.entrySet()
                .stream()
                .filter(e -> e.getValue() > 0)
                .collect(Collectors.toList());

        int i = 0, j = 0;

        while (i < debtors.size() && j < creditors.size()) {

            var debtor = debtors.get(i);
            var creditor = creditors.get(j);

            double amount = Math.min(-debtor.getValue(), creditor.getValue());

            settlements.add(new SettlementDto(
                    debtor.getKey(),
                    creditor.getKey(),
                    round(amount)));

            debtor.setValue(debtor.getValue() + amount);
            creditor.setValue(creditor.getValue() - amount);

            if (debtor.getValue() == 0)
                i++;
            if (creditor.getValue() == 0)
                j++;
        }

        return settlements;
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
