package br.ufc.great.greattour;

import java.util.ArrayList;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import br.ufc.great.loccamlib.LoccamListener;
import br.ufc.great.loccamlib.LoccamManager;
import br.ufc.great.syssu.base.Tuple;
import br.ufc.great.syssu.base.interfaces.IClientReaction;
import br.ufc.great.syssu.base.interfaces.IFilter;
import br.ufc.great.syssu.base.interfaces.ISysSUService;

/**
 * @author Belmondo Rodrigues
 * @author Edmilson Rocha
 *
 *
 */

public class LoccamActivator implements LoccamListener {

	private double value;

	private static LoccamActivator instance;
	private LoccamManager loccam;
	private static Context context;
	private static final String APPID = "Entity";

	//private static final String LightLoCCAM = "context.ambient.light";
	//private static final String AccelerationLoCCAM = "context.device.acceleration";

	private static final String connectivity = "context.device.connectivity";
	private static final String battery = "context.device.bateria";

	private static final String NFC ="context.device.NFC";

	private String RuleExampleWithLightLoCCAM;
	private String RuleBattery;

	private String RuleNFC;
	//private String RuleExampleWithAcceleration;

	private IClientReaction RuleExampleWithLightLoCCAMReaction;
	private IClientReaction RuleBatteryReaction;

	private IClientReaction RuleNFCReaction;
	//private IClientReaction RuleExampleWithAccelerationReaction;

	private IFilter RuleExampleWithLightLoCCAMFilter;
	private IFilter RuleBatteryFilter;

	private IFilter RuleNFCFilter;
	//private IFilter RuleExampleWithAccelerationFilter;


	//private static final String parameterOfnotifyappRuleExample = "Enabled Rule";

	private LoccamActivator(String id) {
		loccam = new LoccamManager(context, APPID, id);


		createReactions();
		createFilters();


	}


	public static LoccamActivator getInstance(Context context, String id) {
		LoccamActivator.context = context;
		if (instance == null) {
			instance = new LoccamActivator(id);
		}
		return instance;
	}

	/**
	 * Nome: parseTuple
	 * Entrada: Tupla correspondente � informa��o contextual que se est� interessado
	 * Sa�da: String com o valor da informa��o contextual
	 * Descri��o: Recebe a tupla vinda do SysSU e retira dela o valor da informa��o contextual correspondente
	 */
	private String parseTuple(Tuple tuple) {
		if (tuple == null) {
			return "ERROR: Reading null";
		} else if (tuple.isEmpty()) {
			return "null";
		} else {
			String s = tuple.getField(2).getValue().toString();
			return s.substring(1, s.length() - 1);
		}
	}

	/**
	 * Nome: connect
	 * Entrada: -
	 * Sa�da: -
	 * Descri��o: M�todo de conex�o da comunica��o com o LoCCAM
	 */
	public void connect() {
		loccam.start(this);
	}

	/**
	 * Nome: connect
	 * Entrada: Um listener da biblioteca LoCCAM_Lib
	 * Sa�da: -
	 * Descri��o: M�todo de conex�o da comunica��o com o LoCCAM
	 */
	public void connect(LoccamListener listener) {
		loccam.start(listener);
	}

	/**
	 * Nome: disconnect
	 * Entrada: -
	 * Sa�da: -
	 * Descri��o: M�todo de desconex�o da comunica��o com o LoCCAM
	 */
	public void disconnect() {
		try {
			loccam.stop();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onServiceConnected(ISysSUService arg0) {
		if (context != null)
			Toast.makeText(context, "Loccam Connected", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onServiceDisconnected() {
		if (context != null)
			Toast.makeText(context, "Loccam Disconnected", Toast.LENGTH_SHORT)
					.show();
	}

	/**
	 * Nome: readLightLoCCAM
	 * Entrada: -
	 * Sa�da: String com o valor da informa��o contextual LightLoCCAM
	 * Descri��o: Realiza a leitura da informa��o contextual LightLoCCAM e retorna o valor da leitura como uma String
	 */
	public String readLightLoCCAM() {
		Tuple tuple = null;

		try {
			tuple = loccam.read(connectivity);

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return parseTuple(tuple);
	}

	/**
	 * Nome: readBattery
	 * Entrada: -
	 * Sa�da: String com o valor da informa��o contextual battery
	 * Descri��o: Realiza a leitura da informa��o contextual battery e retorna o valor da leitura como uma String
	 */

	public String readBattery(){
		Tuple tuple = null;

		try{
			tuple = loccam.read(battery);
		}
		catch(RemoteException e){

			e.printStackTrace();
		}

		return parseTuple(tuple);
	}

	/**
	 * Nome: readNFC
	 * Entrada: -
	 * Sa�da: String com o valor da informa��o contextual NFC
	 * Descri��o: Realiza a leitura da informa��o contextual NFC e retorna o valor da leitura como uma String
	 */

	public String readNFC(){
		Tuple tuple = null;

		try{
			tuple = loccam.read(NFC);
		}
		catch(RemoteException e ){
			e.printStackTrace();
		}

		return parseTuple(tuple);

	}





	/**
	 * Nome: takeLightLoCCAMInterest
	 * Entrada: -
	 * Sa�da: -
	 * Descri��o: Retira o interesse na informa��o contextual LightLoCCAM
	 */
	public void takeLightLoCCAMInterest() {
		try {
			loccam.takeInterest(connectivity);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Nome: takeBatteryInterest
	 * Entrada: -
	 * Sa�da: -
	 * Descri��o: Retira o interesse na informa��o contextual battery
	 */

	public void takeBatteryInterest(){
		try{
			loccam.takeInterest(battery);
		}
		catch(RemoteException e){
			e.printStackTrace();
		}
	}


	/**
	 * Nome: takeNFCInterest
	 * Entrada: -
	 * Sa�da: -
	 * Descri��o: Retira o interesse na informa��o contextual NFC
	 */

	public void takeNFCInterest(){
		try{
			loccam.takeInterest(NFC);
		}
		catch(RemoteException e){
			e.printStackTrace();
		}
		}





	/**
	 * Nome: putLightLoCCAMInterest
	 * Entrada: -
	 * Sa�da: -
	 * Descri��o: Publica o interesse na informa��o contextual LightLoCCAM
	 */
	public void putLightLoCCAMInterest() {
		try {
			loccam.putInterest(connectivity);
			//MainActivity.chamaImprimeMensagemInteresseLight();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * Nome: putBatteryInterest
	 * Entrada: -
	 * Sa�da: -
	 * Descri��o: Publica o interesse na informa��o contextual battery
	 */
	public void putBatteryInterest(){
		try{
			loccam.putInterest(battery);

		}
		catch(RemoteException e){
			e.printStackTrace();
		}
	}


	public void putNFCInterest(){
		try{
			loccam.putInterest(NFC);
		}
		catch(RemoteException e){
			e.printStackTrace();
		}
	}





	public void putCACs() {
		ArrayList<String> list = new ArrayList<String>();
		list.add(connectivity);
		list.add(battery);
		list.add(NFC);
		//list.add(AccelerationLoCCAM);
		//list.add(BatteryLoCCAM);
		try {
			loccam.putCAC(list);
			//MainActivity.chamaImprimeMensagem();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	private void createFilters() {

		/**
		 * Nome: RuleExampleWithLightLoCCAMFilter
		 * Entrada: Uma tupla correspondente � informa��o contextual que est� sendo analisada
		 * Sa�da: Um booleano que indica se a condi��o foi satisfeita ou n�o
		 * Descri��o: Filtro simples correspondente � informa��o contextual [LightLoCCAM]
		 */
		RuleExampleWithLightLoCCAMFilter = new IFilter.Stub() {
			@Override
			public boolean filter(Tuple arg0) throws RemoteException {

				//int LightLoCCAM = Integer.valueOf(parseTuple(arg0));
				//Double LightLoCCAM = Double.valueOf(parseTuple(arg0));
				int connection = Integer.valueOf(parseTuple(arg0));
				//if (LightLoCCAM > 3) {
					System.out.println("value[0]: " + connection);
					return true;
				//} else {
					//return false;
				//}
			}
		};

		RuleBatteryFilter = new IFilter.Stub() {

			@Override
			public boolean filter(Tuple arg0) throws RemoteException {

				int batteryLevel = Integer.valueOf(parseTuple(arg0));
				System.out.print("value[0]:"+ batteryLevel);
				return true;
			}
		};

		RuleNFCFilter = new IFilter.Stub() {

			@Override
			public boolean filter(Tuple arg0) throws RemoteException {

				return true;
			}
		};






	}

	private void createReactions() {

		/**
		 * Nome: RuleExampleWithLightLoCCAMReaction
		 * Entrada: Uma tupla correspondente � informa��o contextual LightLoCCAM
		 * Sa�da: -
		 * Descri��o: A rea��o correspondente ao fato da condi��o ter sido satisfeita
		 */
		RuleExampleWithLightLoCCAMReaction = new IClientReaction.Stub() {

			@Override
			public void react(Tuple arg0) throws RemoteException {
				int value = Integer.parseInt(parseTuple(arg0));
				//value = Double.parseDouble(parseTuple(arg0));
				//System.out.println("value: " + value);
				System.out.println("Cheguei no 1 reaction");

				LoginActivity.exibeconexao(value);


			}


		};

		RuleBatteryReaction = new IClientReaction.Stub() {

			@Override
			public void react(Tuple arg0) throws RemoteException {
				int value = Integer.parseInt(parseTuple(arg0));

				System.out.println("Cheguei no 2 reaction");

				if (value<=10){

					CurrentRoomActivity.lessThenTenPercentBattery();
				}else if(value<=30 && value>=20){
					FilesActivity.between20e30();

				}else if(value<=20 && value>=10){
					FilesActivity.between10e20();

				}

			}
		};






	}

	/**
	 * Nome: notifyappRuleExample
	 * Entrada: Uma String com o texto a aparecer no Log
	 * Sa�da: -
	 * Descri��o: Imprime um Log com a mensagem
	 */
	public double getValue() {
		return value;
	}


	/**
	 * Nome: listenConditionRuleExample
	 * Entrada: Um booleano indicando se a regra deve ou n�o ser ativada
	 * Sa�da: -
	 * Descri��o: Quando recebe um verdadeiro, ativa a regra RuleExample e todas as subscri��es �s informa��es contextuais contidas nesta
	 */
	public void listenConditionRuleExample(boolean command) {
		if (command) {

			Log.d("Teste", "Entrou listen");
			System.out.println("Entrou Listener");

			try {
				RuleExampleWithLightLoCCAM = loccam.subscribe(
						RuleExampleWithLightLoCCAMReaction, "put", connectivity,
						RuleExampleWithLightLoCCAMFilter);



			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			if (RuleExampleWithLightLoCCAM != null) {
				try {
					loccam.unSubscribe(RuleExampleWithLightLoCCAM);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}




	public void listenConditionBattery(boolean command){
		if(command){
			Log.d("Teste2", "Entrou no Segundo Listener");
			System.out.println("Entrou no Segundo Listener");

			try{
				RuleBattery = loccam.subscribe(RuleBatteryReaction, "put", battery,
						RuleBatteryFilter);
			} catch(RemoteException e){

				e.printStackTrace();
			}

		} else{
				if(RuleBattery != null){
				try{

					loccam.unSubscribe(RuleBattery);

				} catch(RemoteException e){

					e.printStackTrace();
				}
			}

		}


		}


	}










