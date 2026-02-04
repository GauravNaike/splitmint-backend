package com.karbon.splitmint.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.karbon.splitmint.service.GroupService;

import lombok.RequiredArgsConstructor;

import com.karbon.splitmint.model.Expense;
import com.karbon.splitmint.model.Group;
import com.karbon.splitmint.model.Participant;
import com.karbon.splitmint.dto.AddExpenseRequest;
import com.karbon.splitmint.dto.BalanceDto;
import com.karbon.splitmint.dto.ParticipantResponseDto;
import com.karbon.splitmint.dto.SettlementDto;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    public Group createGroup(@RequestBody Map<String, String> request) {
        return groupService.createGroup(request.get("name"));
    }

    @GetMapping
    public List<Group> getGroups() {
        return groupService.getAllGroups();
    }

    @GetMapping("/{groupId}")
    public Group getGroup(@PathVariable String groupId) {
        return groupService.getGroupById(groupId);
    }

    @PostMapping("/{groupId}/participants")
    public ParticipantResponseDto addParticipant(
            @PathVariable String groupId,
            @RequestBody Map<String, String> request) {

        Participant p = groupService.addParticipant(groupId, request.get("name"));

        return new ParticipantResponseDto(p.getId(), p.getName());
    }

    @PostMapping("/{groupId}/expenses")
    public Expense addExpense(
            @PathVariable String groupId,
            @RequestBody AddExpenseRequest request) {

        return groupService.addExpense(
                groupId,
                request.getDescription(),
                request.getAmount(),
                request.getPaidBy(),
                request.getParticipantIds());
    }

    @GetMapping("/{groupId}/balances")
    public List<BalanceDto> getBalances(@PathVariable String groupId) {
        return groupService.calculateBalances(groupId);
    }

    @GetMapping("/{groupId}/settlements")
    public List<SettlementDto> getSettlements(@PathVariable String groupId) {
        return groupService.calculateSettlements(groupId);
    }

}
