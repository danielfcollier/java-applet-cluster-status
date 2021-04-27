// ------------------------------------------------------------------
// NodeObject.java
// ------------------------------------------------------------------

/*
 * ===============================================
 *
 * Copyright (c) 2006 Daniel Augusto F Collier. 
 * ===============================================
 */

import java.net.URL;
import java.util.Properties;
import javax.swing.*;

/**
 * Processa toda a informação referente aos nós determinando a partir da mensagem
 * e dos eventos processados como os nós serão desenhados na tela.
 *
 * @author Daniel Augusto F Collier
 * @author <a href="mailto:collier@linse.ufsc.br">collier@linse.ufsc.br</a>
 * @author <a target="_blank" href="http://www.linse.ufsc.br">www.linse.ufsc.br</a>
 * @version 1.0
 */

public class NodeObject {
// ------------------------------------------------------------------
    /**
     * Tempo de atualização em milissegundos.
     */
    public int TIMER_REFRESH;
    /**
     * Número máximo de nós exibidos.
     */
    public int NNO;
    /**
     * Número máximo de cpus exibidas.
     */
    public int NCPU;
    /**
     * Tempo de espera da resposta do servidor em milissegundos.
     */
    public int TIME_OUT;
    /**
     * Tamanho da mensagem no servidor.
     */
    public int BYTES;
    /**
     * Valor filtrado pelo combo box.
     */
    public int cmb_filtro=0;
    /**
     * Barra de carga.
     */    
    public ImageIcon LoadBar;
    private ClassLoader classLoader;
    private URL appIconUrl;
    
    /**
     * Vetor com o nome de todos os nós.
     */
    public String nNames[];
    /**
     * Matriz em que as linhas correspondem aos nós
     * e as colunas as cargas em cada nó.
     */
    public int NodeStatus[][];
    /**
     * Matriz em que as linhas correspondem aos nós
     * e as colunas as cargas em cada nó.
     */    
    public String strStatus[][];
    
    /**
     * Opção de exibição dos nós dados como "DEAD".
     */
    public boolean ShowDead = false;
    /**
     * Marca os nós dados como "DEAD".
     */
    public boolean IsDead[];
    /**
     * Marca os nós foram selecionados.
     */    
    public boolean IsSelected[];
    /**
     * Marca os nós com exibição expandida .
     */
    public boolean IsExpanded[];
    /**
     * Linhas em que serão desenhadas os nós.
     */    
    public int line[];
    /**
     * Região que o nó ocupa no painel.
     */ 
    public int area[][];
    
    private String LOADBAR = "loadbar2.gif"; 
    private ClientX Client = new ClientX();
    
// ------------------------------------------------------------------
    /**
     *  Inicialização e alocação de memória para as variáveis. 
     *
     */     
    public NodeObject( Config config ) {
        TIMER_REFRESH = config.TIMER_REFRESH;
        NNO = config.NNO;
        NCPU = config.NCPU;
        TIME_OUT = config.TIME_OUT;
        BYTES = config.BYTES;
               
        int Int1[] = new int[NNO];
        int Int21[][] = new int[NNO][NCPU+1];
        int Int22[][] = new int[NNO][NCPU+1];
        String Str1[] = new String[NNO];
        String Str2[][] = new String[NNO][NCPU+1];
        boolean Boo1[] = new boolean[NNO];
        boolean Boo2[] = new boolean[NNO];
        boolean Boo3[] = new boolean[NNO];
        
        line = Int1;
        area = Int21;
        NodeStatus = Int22;
        
        nNames = Str1;
        strStatus = Str2;
        
        IsDead = Boo1;
        IsSelected = Boo2;
        IsExpanded = Boo3;
        
        ClassLoader classLoader = this.getClass().getClassLoader();
        appIconUrl = classLoader.getResource( LOADBAR );
        LoadBar = new ImageIcon( appIconUrl );
        
        for ( int i=0; i<NNO; i++ ) {
            IsExpanded[i] = false;
            IsSelected[i] = false;
            IsDead[i] = false;
            line[i] = 1;
        }
        
        for ( int i=0; i < NNO; i++ ) {
            if ( i >= 9 )
                nNames[i] = "node" + Integer.toString( i+1 );
            
            else
                nNames[i] = "node0" + Integer.toString( i+1 );
        }
    }
// ------------------------------------------------------------------
    /**
     * Processa a messagem recebida do servidor ajustando as variáveis 
     * que definem as propriedades de cada nó.
     *
     */         
    public void GetInfo() {
        Properties CpuLoads = new Properties();
        CpuLoads = Client.ClientX( BYTES, TIME_OUT ); // dicionário com a mensagem processada
        String temp;
        
        // reinicializa toda a informação das variáveis em uso.
        for ( int i=0; i<NNO; i++ )
            for ( int j=0; j<(NCPU+1); j++ )
                area[i][j] = -1; 
        
        for ( int i=0; i<NNO; i++ ) {
            IsDead[i] = false; 
            line[i] = 1;
            
            if ( !CpuLoads.containsKey(nNames[i]) ) {
                IsDead[i] = true;
                IsExpanded[i] = false;
                NodeStatus[i][0] = -1;
            } else {
                temp = (String) CpuLoads.getProperty( nNames[i] );
                strStatus[i] = temp.split(" ");
                
                for ( int j=0; j<NodeStatus[i].length; j++ ) {
                    if ( j < strStatus[i].length )
                        NodeStatus[i][j] = Integer.valueOf( strStatus[i][j] ).intValue();
                    else
                        NodeStatus[i][j] = -1;
                }
            }
           
            //------------------------------
            if ( NodeStatus[i][0] < cmb_filtro ) {
                NodeStatus[i][0] = -1;
                IsDead[i] = true;
            }     
            //------------------------------
            
            if ( i!=0 ) {
                if ( ShowDead ) {
                    if ( IsExpanded[i-1] )
                        line[i] = line[i-1] + strStatus[i-1].length;
                    else
                        line[i] = line[i-1] + 1;
                } else {
                    if ( IsDead[i-1] )
                        line[i] = line[i-1];
                    else if ( IsExpanded[i-1] )
                        line[i] = line[i-1] + strStatus[i-1].length;
                    else
                        line[i] = line[i-1] + 1;
                }
            }
        }
         
        for ( int i=0; i<NNO; i++ ) {
            if ( i==(NNO-1) ) {
                int var = ( line[i] + (strStatus[i].length) *
                        boo2int(IsExpanded[i])+1*( boo2int(!IsExpanded[i])) );
                
                for ( int j=line[i]; j < var ; j++)
                    area[i][j-line[i]] = j;
            } else if ( line[i] == line[i+1] )
                area[i][0] = -1;
            else  {
                for ( int j=line[i]; j<line[i+1]; j++)
                    area[i][j-line[i]] = j;
            }
        }
    }
// ------------------------------------------------------------------
    /**
     *  Converte um valor de booleano para binário.
     *  @param x true ou false
     *  @return o valor em inteiro correspondente ao valor booleano
     *
     */        
    public int boo2int( boolean x ) {
        if ( x )
            return 1;
        else
            return 0;
    }
// ------------------------------------------------------------------
}
