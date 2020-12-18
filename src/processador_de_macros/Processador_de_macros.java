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
            String[] line_args;
            String line_definicao;
            String[] line_label;
            for(int i = 0; i < conteudo.size(); ++i){
                if (conteudo.get(i).compareTo("MCDEFN") == 0){
                    for(j = i+1; conteudo.get(j).compareTo("MCEND") != 0 || j > conteudo.size(); ++j){
                        
                        line_args = conteudo.get(j).split(" ", 2);
                        line_definicao = conteudo.get(j);
                        line_label = conteudo.get(j).split(": ");
                        
                        //============================
                        //TRATAMENTO DA PRIMEIRA LINHA
                        //============================
                        
                        //PEGA OS LABELS
                        if(macro.nome == null){
                            if (line_label.length > 1){
                                line_args = line_label[1].split(" ", 2);
                                line_label = line_label[0].split(",");
                                for(int l = 0; l < line_label.length; ++l){
                                    line_label[l] = line_label[l].replace(" ", "");
                                    macro.labels.add(line_label[l]);
                                }
                            }
                            
                            //PEGA O NOME DA MACRO
                            macro.nome = line_args[0];
                            
                            //PEGA OS ARGUMENTOS
                            
                            line_args = line_args[1].split(",");
                            for(int l = 0; l < line_args.length; ++l){
                                line_args[l] = line_args[l].replace(" ", "");
                                macro.argumentos.add(line_args[l]);
                            }
                        }
                        //PEGA A DEFINICAO DA MACRO
                        else {
                            macro.definicao.add(line_definicao);
                        }
                    }

                    System.out.println(macro.nome);
                    System.out.println(macro.definicao);
                    System.out.println(macro.argumentos);
                    System.out.println(macro.labels);
                    
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
