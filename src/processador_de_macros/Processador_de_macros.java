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
        
        Macro macro = new Macro();
        
        //INICIALIZA ARQUIVA DE ENTRADA
        File arquivo_entrada = new File(diretorio);
        File arquivo_saida = new File("arquivo_saida.asm");
        
        //VETOR QUE ARMAZENA O CONTEUDO DO ARQUIVO DE ENTRADA
        List<String> conteudo = new ArrayList<>();
        
        
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
            
            //============================
            //PROCESSADOR DE MACROS EM SI:
            //============================
            
            //1 PASSAGEM - DEFINIÇÃO
            //PEGA A DEFINIÇÃO DE MACRO
            int j;
            String[] label_macro;
            for(int i = 0; i < conteudo.size(); ++i){
                if (conteudo.get(i).compareTo("MCDEFN") == 0){
                    for(j = i+1; conteudo.get(j).compareTo("MCEND") != 0 || j > conteudo.size(); ++j){
                        
                        label_macro = conteudo.get(j).split(" ");
                        
                        //PEGA OS ARGUMENTOS
                        if(macro.nome == null){
                            macro.nome = label_macro[0];
                            for(int l = 1; l < label_macro.length; ++l){
                                macro.argumentos.add(label_macro[l]);
                            }
                        }
                        
                        //PEGA A DEFINICAO DA MACRO
                        for (int k = 0; k < label_macro.length; ++k){
                            macro.definicao.add(label_macro[k]);
                        }
                    }
                    
                    
                    //System.out.println(conteudo.subList(i, j));
                    System.out.println(macro.nome);
                    
                    conteudo.removeAll(conteudo.subList(i, j));
                }
                else {
                    escrever.write(conteudo.get(i));
                    escrever.newLine();
                }
            }
            
            
            //2 PASSAGEM - EXPANSÃO
            //=====================
            
            
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
