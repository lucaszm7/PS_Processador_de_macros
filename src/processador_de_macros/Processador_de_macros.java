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
        
        List<Macro> macros = new ArrayList<>();
        
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
                //SEM LINHA VAZIA
                //if(!line.isEmpty()){
                    conteudo.add(line);
                //}
                
            }
            
            //============================
            //PROCESSADOR DE MACROS EM SI:
            //============================
            
            //1 PASSAGEM - DEFINIÇÃO
            //PEGA A DEFINIÇÃO DE MACRO
            String[] line_args;
            String line_definicao;
            String[] line_label;
            for(int i = 0; i < conteudo.size(); i++){
                Macro macrinho = new Macro();
                if (conteudo.get(i).compareTo("MCDEFN") == 0){
                    macrinho.MCDEFN = i;
                    for(int j = i+1; conteudo.get(j).compareTo("MCEND") != 0; j++){
                        
                        line_args = conteudo.get(j).split(" ", 2);
                        line_definicao = conteudo.get(j);
                        line_label = conteudo.get(j).split("&");
                        //============================
                        //TRATAMENTO DA PRIMEIRA LINHA
                        //============================
                        
                        //PEGA OS LABELS
                        if(macrinho.nome == null){
                            if (line_label.length > 1){
                                //line_label = line_label[1].split(" ", 2);
                                line_args = line_label[line_label.length - 1].split(" ", 2);
                                line_args = line_args[1].split(" ", 2);
                                //line_label = line_label[0].split(",");
                                
                                for(int l = 1; l < line_label.length - 1; ++l){
                                    line_label[l] = line_label[l].replace(" ", "");
                                    line_label[l] = line_label[l].replace(",", "");
                                    macrinho.labels.add(line_label[l]);
                                }
                                line_label = line_label[line_label.length - 1].split(" ", 2);
                                line_label[0] = line_label[0].replace(" ", "");
                                line_label[0] = line_label[0].replace(",", "");
                                macrinho.labels.add(line_label[0]);
                            }
                            //PEGA O NOME DA MACRO
                            macrinho.nome = line_args[0];
                            
                            //PEGA OS ARGUMENTOS
                            
                            line_args = line_args[1].split(",");
                            for(int l = 0; l < line_args.length; ++l){
                                line_args[l] = line_args[l].replace(" ", "");
                                macrinho.argumentos.add(line_args[l]);
                            }
                        }
                        //PEGA A DEFINICAO DA MACRO
                        else {
                            line_definicao = line_definicao.replace("&", "");
                            macrinho.definicao.add(line_definicao);
                        }
                        macrinho.MCEND = j+1;
                    }
                    macros.add(macrinho); 
                }
            }
            
            for (int i = macros.size() - 1; i >= 0; --i){
                //System.out.println("I = " + i);
                //System.out.println("MCDEFN = " + macros.get(i).MCDEFN);
                //System.out.println("MCEND = " + macros.get(i).MCEND);
                conteudo.removeAll(conteudo.subList(macros.get(i).MCDEFN, macros.get(i).MCEND));
            }

            //conteudo.removeAll(conteudo.subList(i, piranha+1));
            
            //=====================
            //2 PASSAGEM - EXPANSÃO
            //=====================
            
            boolean flag_chamada = false;
            
            for (int i = 0; i < conteudo.size(); ++i){
                Macro macro_chamada = new Macro();
                String[] aux_labels = conteudo.get(i).split("&");
                String[] aux_linha = conteudo.get(i).split(" ", 2);
                
                //SE TEM LABEL
                if (aux_labels.length > 1){
                    for (int j = 1; j < aux_labels.length - 1; ++j){
                        macro_chamada.labels.add(aux_labels[j]);
                    }
                    
                    aux_linha = aux_labels[aux_labels.length - 1].split(" ", 2);
                    aux_linha = aux_linha[aux_labels.length - 1].split(" ", 2);
                    
                    aux_labels = aux_labels[aux_labels.length - 1].split(" ", 2);
                    macro_chamada.labels.add(aux_labels[0]);
                }
                
                for (Macro iterator : macros) {
                    
                    //SE NÃO TEM LABEL
                    if (aux_linha[0].compareTo(iterator.nome) == 0){
                        flag_chamada = true;
                        
                        //PASSA OS ARGUMENTOS P/ A CHAMADA
                        macro_chamada.nome = aux_linha[0];

                        String[] aux_argumentos = aux_linha[1].split(",");
                        for(int l = 0; l < aux_argumentos.length; ++l){
                            aux_argumentos[l] = aux_argumentos[l].replace(" ", "");
                            macro_chamada.argumentos.add(aux_argumentos[l]);
                        }

                        //PASSA A DEFINIÇÃO P/ A CHAMADA
                        for(int l = 0; l < iterator.definicao.size(); ++l){
                            macro_chamada.definicao.add(iterator.definicao.get(l));
                            //System.out.println(macro_chamada.definicao.get(l));
                        }

                        //SUBSTITUI OS ARGUMENTOS
                        for(int l = 0; l < macro_chamada.definicao.size(); ++l){
                            //macro_chamada.definicao.set(l, macro_chamada.definicao.get(l).replace(macro.labels.get(l), macro_chamada.labels.get(l)));
                            for (int k = 0; k < macro_chamada.argumentos.size(); ++k){
                                String replace = macro_chamada.definicao.get(l).replace(iterator.argumentos.get(k), macro_chamada.argumentos.get(k));
                                //System.out.println("L = " + l);
                                //System.out.println("K = " + k);
                                //System.out.println("REPLACE = " + replace);
                                //System.out.println("CHAMADA DEFINICAO(K) = " + macro_chamada.definicao.get(l));
                                if (replace.compareTo(macro_chamada.definicao.get(l)) != 0){
                                    //System.out.println("DIFERENTE");
                                    macro_chamada.definicao.set(l, replace);
                                }
                            }
                            //System.out.println(macro_chamada.definicao.get(l).replace("ARG1", "BUNDA"));
                        }
                        
                        //SUBSTITUI AS LABELS
                        for(int l = 0; l < macro_chamada.labels.size(); ++l){
                            for (int k = 0; k < macro_chamada.labels.size(); ++k){
                                
                                String replace = macro_chamada.definicao.get(l).replace(iterator.labels.get(k), macro_chamada.labels.get(k));
                                if (replace.compareTo(macro_chamada.definicao.get(l)) != 0){
                                    macro_chamada.definicao.set(l, replace);
                                }
                            }
                        }

                        //EXPANDE A MACRO
                        for(int l = 0; l < macro_chamada.definicao.size(); ++l){
                            escrever.write(macro_chamada.definicao.get(l));
                            escrever.newLine();
                        }
                    }
                }
                //NÃO É UMA CHAMADA DE MACRO
                //ESCREVENDO NO ARQUIVO DE SAIDA
                if (!flag_chamada) {
                    escrever.write(conteudo.get(i));
                    escrever.newLine();
                }
                flag_chamada = false;
            }
            
            
            
            macros.forEach(iterator -> {
                iterator.print();
            });
            //=========================
            //SALVANDO ARQUIVO DE SAIDA
            //=========================
            escrever.close();
            fileWriter.close();
        }
        
        catch(IOException ex){
            System.out.println(ex);
        }
        
        return arquivo_saida;
    } 
    
}
