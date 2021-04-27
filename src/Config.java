// ------------------------------------------------------------------
// Config.java
// ------------------------------------------------------------------

/*
 * ===============================================
 *
 * Copyright (c) 2006 Daniel Augusto F Collier. 
 * ===============================================
 */

import java.io.*;
import java.io.IOException;
import java.lang.Integer;

/**
 * L� o arquivo de configura��o .Jclusterstatus.rc que 
 * est� no diret�rio HOME do us�rio, caso contr�rio 
 * utiliza como padr�o os valores definidos na classe. 
 *
 * @author Daniel Augusto F Collier
 * @author <a href="mailto:collier@linse.ufsc.br">collier@linse.ufsc.br</a>
 * @author <a target="_blank" href="http://www.linse.ufsc.br">www.linse.ufsc.br</a>
 * @version 1.0
 */

public class Config {
// ------------------------------------------------------------------    
    /**
     * Tempo de atualiza��o em milissegundos.
     */
    public int TIMER_REFRESH = 500;
    /**
     * N�mero m�ximo de n�s exibidos.
     */
    public int NNO = 55;
    /**
     * N�mero m�ximo de cpus exibidas.
     */    
    /**
     * Tempo de espera da resposta do servidor em milissegundos.
     */ 
    public int NCPU = 5;
    /**
     * Tempo de espera da resposta do servidor em milissegundos.
     */   
    public int TIME_OUT = 20;
    /**
     * Tamanho da mensagem no servidor.
     */  
    public int BYTES = 1024;
    
    private File name;
    private BufferedReader file;
    
    private String fileInf[] = new String[5];
// ------------------------------------------------------------------  
     /**
     *  
     * Se existir o arquivo de configura��o .Jclusterstatus.rc
     * no diret�rio HOME, os valores das vari�veis s�o 
     * redefinidos conforme seus nomes.
     * @throws IOException usa como padr�o os valores definidos na 
     * inicializa��o da classe
     */        
    public Config() throws IOException {
        
        File name = new File( System.getProperty("user.home") 
            + File.separator + ".Jclusterstatus.rc" );
        
        String temp[];
        String var;
        
        Integer var2;
        int value;
        
        if ( name.exists() ) {          // verifica se existe arquivo de configura��o
            try {
                file = new BufferedReader( new FileReader( name ) );
                
                // Leitura de cinco linhas do arquivo em busca das vari�veis no arquivo.
                for ( int i=0; i<5; i++ ) {
                    fileInf[i] = file.readLine();
                    temp = fileInf[i].split( "=" );
                    value = Integer.valueOf( temp[1] ).intValue();
                    var = temp[0];
                                  
                    if ( var.compareTo( "TIMER_REFRESH" ) == 0 )
                        TIMER_REFRESH = value;
                    
                    else if ( var.compareTo( "NNO" ) == 0 )
                        NNO = value;
                    
                    else if ( var.compareTo( "NCPU")== 0 )
                        NCPU = value;
                    
                    else if ( var.compareTo( "TIME_OUT" ) == 0 )
                        TIME_OUT = value;
                    
                    else if ( var.compareTo( "BYTES" ) == 0 )
                        BYTES = value;
                }
            } catch ( FileNotFoundException ex ) {}
        }
    }
}
// ------------------------------------------------------------------