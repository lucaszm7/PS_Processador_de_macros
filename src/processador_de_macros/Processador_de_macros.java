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
        List<Macro> macros = new ArrayList<>();
        
        File arquivo_entrada = new File(diretorio);
        File arquivo_saida = new File("arquivo_saida.asm");
        List<String> conteudo = new ArrayList<>();
        
        //PARA ARMAZENAR AS MACROS
        Map<String, List<String>> tabela_de_macros = new HashMap<String, List<String>>();
        
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
            
            //PEGA A DEFINIÇÃO DE MACRO
            int j;
            String[] label_macro;
            for(int i = 0; i < conteudo.size(); ++i){
                if (conteudo.get(i).compareTo("MCDEFN") == 0){
                    for(j = i; conteudo.get(j).compareTo("MCEND") != 0 || j > conteudo.size(); ++j){
                        
                    }
                    ++j;
                    label_macro = conteudo.get(i).split(" ");
                    tabela_de_macros.put(label_macro[0], conteudo.subList(i+1, j-1));
                    
                    System.out.println(conteudo.subList(i, j));
                    
                    conteudo.removeAll(conteudo.subList(i, j));
                        
                    //System.out.println(tabela_de_macros);
                }
            }
            
            
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
