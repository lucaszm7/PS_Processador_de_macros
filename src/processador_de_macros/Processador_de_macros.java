package processador_de_macros;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.Map;
import java.util.HashMap;

public class Processador_de_macros {

    public static File run(String diretorio){
        
        //INICIALIZA ARQUIVA DE ENTRADA
        File arquivo_entrada = new File(diretorio);
        File arquivo_saida = new File("arquivo_saida.asm");
        List<String> conteudo = new ArrayList<>();
        
        //PARA ARMAZENAR AS MACROS
        Map<String, String> tabela_de_macros = new HashMap<String, String>();
        
        try {
            //INICIALIZA ARQUIVO DE SAIDA
            
            arquivo_saida.createNewFile();
            
            //LEITURA ARQUIVO DE ENTRADA
            Scanner reader = new Scanner(arquivo_entrada);
            
            //PARA ESCREVER NO ARQUIVO DE SAIDA
            FileWriter fileWriter = new FileWriter(arquivo_saida, false);
            BufferedWriter escrever = new BufferedWriter(fileWriter);
            
            //COPIANDO ARQUIVO DE ENTRADA PARA UM VETOR
            //COLOCANDO P/ UPPER CASE
            
            while(reader.hasNextLine()) {
                String line = reader.nextLine();
                line = line.toUpperCase();
                //String[] line_coment = line.split("//");
                //String[] line_word = line.split(" ");
                conteudo.add(line);
                
            }
            
            System.out.println(conteudo.get(0));
            
            //============================
            //PROCESSADOR DE MACROS EM SI:
            //============================
            
            String teste = new String();
            teste = teste.concat("TESTE");
            
            
            //ESCREVENDO NO ARQUIVO DE SAIDA
            for (String iterator : conteudo) {
                escrever.write(iterator);
                escrever.newLine();
            }
            
            //SALVANDO ARQUIVO DE SAIDA
            escrever.close();
            fileWriter.close();
            
        }
        catch(IOException ex){
            System.out.println(ex);
        }
        
        return arquivo_saida;
    } 
    
}
