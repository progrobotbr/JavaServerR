import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import utils.Utils;

//import client.ClientCommand;
import client.ClientManageCreateModule;
import client.ClientManageResponse;
//import client.ClientRequest;
import client.ClientSession;
import client.ClientProtocol; 
import client.ServerCommand;
import dictionary.ComponentsDictionary;

public class GuiBK1 {

	//Objetos Globais
	private HashMap mModulo;
	private JFrame mGui;
	private ClientSession mSession;
	private ClientBrowserTransaction mCMCM;
	private ClientProtocol mProtocol;
	
	//Entrada da aplicação
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
	}

	//Exibe a Gui
	private static void createAndShowGUI() {
	
		GuiBK1 oGui = new GuiBK1();
		oGui.Start();
	}

/*
 * 
 *  Processamento inicial
 * 
 */
	
	//Inicio
	public void Start(){
		boolean b;
		int rc;
		
		this.Init();
		
		//Conecta no servidor
		b=this.ConnectToServer();
		if(!b){
		  Utils.log("Não conectou");
		  System.exit(1);
		}
		
		//Executa handshake
		rc=mProtocol.Hello();
		if(rc==ClientProtocol.ERR ){
			Utils.log("Erro em handshake de saudação");
			System.exit(1);
		}
		
		//Chama Logon. Se o logon não for efetuado, não será possível navegar nas outras transações
		/*
		rc=mProtocol.Logon();                         
		 
		if(rc==ClientProtocol.ERR){
			Utils.log("Erro em  chamada ao logon");
			System.exit(1);
		}*/
		
		//Verifica comando enviado pelo servidor
		//   NEWTRZ informa que será iniciado nova transação
		String cm = mProtocol.getServerCommand();
		if(cm.equals(IServerCommand.NEWTRZ)){
			
			//Pega a estrutura do módulo e cria objetos Gui locais
			b = this.createModulo();
			if(!b){
				Utils.log("Erro ao montar estrutura do modulo");
				System.exit(1);
			}
			
			//Pega a tela inicial da transação e executa o request
			rc=mProtocol.CallScreen(mModulo);
			if(rc==ClientProtocol.ERR){
				Utils.log("Erro ao receber dados da tela de logon");
				System.exit(1);
			}
			
			//Carrega telas do módulo
			this.loadResponse();
			this.Show();
		}
	}
	
	//Inicializa objetos básicos e conecta com o servidor
	public void Init(){
		boolean b;
		mModulo = new HashMap();
		mGui = new JFrame("Clien RenServer");
		mGui.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		mSession = new ClientSession();
		mCMCM = new ClientBrowserTransaction(mSession);
		mProtocol = new ClientProtocol(mSession);
	}
	
	//Conexão com o servidor
	public boolean ConnectToServer(){
		boolean b;
		b=mSession.connect("localhost",5555);
		return(b);
	}
		
	public HashMap getMemModulo(){
		return(mModulo);
	}
	
	private void loadResponse(){
		String sGotoScreen;
		sGotoScreen = ClientHandleResponse.loadResponse(mSession, mModulo);
		this.setScreen(sGotoScreen);
	}
	
	private boolean createModulo(){
		boolean bRet=false;
		mCMCM.init();
		bRet = mCMCM.execute();
		if(bRet == true){
			mModulo = mCMCM.getModulo();
		}
		return(bRet);
	}	
	
	public void setScreen(String pScreen){
		HashMap hScreen;
		JPanel oPn;
		hScreen = (HashMap) mModulo.get(pScreen);
		oPn = (JPanel) hScreen.get(ComponentsDictionary.SCR_ + pScreen);
		mGui.add(oPn);
	}

	public void Show(){
		mGui.setSize(300,300);
		mGui.setVisible(true);
	}
	
}
