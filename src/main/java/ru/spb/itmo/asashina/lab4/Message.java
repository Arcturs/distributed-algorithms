package ru.spb.itmo.asashina.lab4;

import ru.spb.itmo.asashina.lab4.operation.Operation;

import java.util.List;

public record Message(List<Operation> operations) {

}
