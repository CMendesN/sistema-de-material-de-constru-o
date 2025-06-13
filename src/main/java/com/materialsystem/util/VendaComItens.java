package com.materialsystem.util;

import com.materialsystem.entity.ItemVenda;
import com.materialsystem.entity.Venda;
import java.util.List;

public record VendaComItens(Venda venda, List<ItemVenda> itens) {}

