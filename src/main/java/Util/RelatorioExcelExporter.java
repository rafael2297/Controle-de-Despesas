package Util;

import Model.Relatorio;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class RelatorioExcelExporter {

    public static void exportarParaExcel(List<Relatorio> relatorios, String nomeArquivo) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Relatório");

            // Estilo para o cabeçalho
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);

            // Estilo para dados textuais
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);
            dataStyle.setBorderTop(BorderStyle.THIN);

            // Estilo para valores numéricos (R$)
            CellStyle valorStyle = workbook.createCellStyle();
            valorStyle.setBorderBottom(BorderStyle.THIN);
            valorStyle.setBorderLeft(BorderStyle.THIN);
            valorStyle.setBorderRight(BorderStyle.THIN);
            valorStyle.setBorderTop(BorderStyle.THIN);
            DataFormat format = workbook.createDataFormat();
            valorStyle.setDataFormat(format.getFormat("#,##0.00")); // Ex: 1.234,56

            // Cabeçalho
            Row header = sheet.createRow(0);
            String[] colunas = { "Data", "Categoria", "Valor", "Descrição", "Pagamento" };
            for (int i = 0; i < colunas.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(colunas[i]);
                cell.setCellStyle(headerStyle);
            }

            // Preenchendo os dados
            int linha = 1;
            BigDecimal somaTotal = BigDecimal.ZERO;

            for (Relatorio r : relatorios) {
                Row row = sheet.createRow(linha++);

                Cell cellData = row.createCell(0);
                cellData.setCellValue(r.getData().toString());
                cellData.setCellStyle(dataStyle);

                Cell cellCategoria = row.createCell(1);
                cellCategoria.setCellValue(r.getNomeCategoria());
                cellCategoria.setCellStyle(dataStyle);

                Cell cellValor = row.createCell(2);
                BigDecimal valor = r.getValor(); // Supondo que seja BigDecimal
                cellValor.setCellValue(valor.doubleValue());
                cellValor.setCellStyle(valorStyle);

                Cell cellDescricao = row.createCell(3);
                cellDescricao.setCellValue(r.getDescricao() != null ? r.getDescricao() : "");
                cellDescricao.setCellStyle(dataStyle);

                Cell cellPagamento = row.createCell(4);
                cellPagamento.setCellValue(r.getPagamento() != null ? r.getPagamento() : "");
                cellPagamento.setCellStyle(dataStyle);

                somaTotal = somaTotal.add(valor);
            }

            // Linha de total
            Row totalRow = sheet.createRow(linha++);
            Cell cellTotalLabel = totalRow.createCell(1);
            cellTotalLabel.setCellValue("Total:");
            cellTotalLabel.setCellStyle(headerStyle);

            Cell cellTotalValue = totalRow.createCell(2);
            cellTotalValue.setCellValue(somaTotal.doubleValue());
            cellTotalValue.setCellStyle(valorStyle);

            // Ajusta a largura das colunas
            for (int i = 0; i < colunas.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Escreve o arquivo
            try (FileOutputStream fileOut = new FileOutputStream(nomeArquivo)) {
                workbook.write(fileOut);
            }

            System.out.println("✅ Relatório exportado com sucesso para: " + nomeArquivo);
            System.out.println("Salvando em: " + new java.io.File(nomeArquivo).getAbsolutePath());

        } catch (IOException e) {
            System.err.println("Erro ao gerar o arquivo Excel: " + e.getMessage());
        }
    }
}
