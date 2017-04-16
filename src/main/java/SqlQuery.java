import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class pressing
{
	int id;
	String nom, addr ;
	int status = 0;

	public pressing(int a, String b, String c, int d)
	{
		id = a;
		nom = b;
		addr = c;
		status = d;
	};

}


public class SqlQuery {
	
	public SqlQuery(){}
	
	
	
	@SuppressWarnings("finally")
	public Statement seConnecter() throws Exception {
		
		String driv = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/xpressing";
		Statement statement = null;		
		try{
		Class.forName(driv);
		Connection connect = DriverManager.getConnection(url,"root","");
		statement = connect.createStatement();
		}catch(Exception e){
			throw e;
		}
		finally {
			return statement;
		}
	}
	
	private ResultSet execution(String req, Statement statement) throws Exception{
		return statement.executeQuery(req);	
	}
	
	private int updateReq(String req, Statement statement) throws Exception{
		return statement.executeUpdate(req);
	}
		
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////LES SELECT FROM//////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	

 
	
	public String userPword(String numero) throws Exception{
		String pword = "notFound";
		Statement stm = seConnecter();
		ResultSet result = execution("SELECT pword FROM users WHERE user = '"+numero+"'", stm);
		if(result.next())pword = result.getString("pword");
		result.close();
		stm.close();
		return pword;
	}
	
	
	public ArrayList<String> allUsers() throws Exception{
		ArrayList<String> users = new ArrayList<String>();
		Statement stm = seConnecter();
		ResultSet result = execution("SELECT * FROM users", stm);
		while(result.next())
			{users.add(result.getString("user"));}
		result.close();
		stm.close();
		return users;
	}
	
    	public int getLastUId() throws Exception{

		int count = 0;
		Statement stm = seConnecter();
		ResultSet result = execution("SELECT * FROM clients", stm);
		while(result.next())
			 {

				count = result.getInt("id");

	         }

		result.close();
		stm.close();
		return count;
	}
	

    	public int depotsAll2() throws Exception{

		int count = 0;
		Statement stm = seConnecter();
		ResultSet result = execution("SELECT * FROM depots", stm);
		while(result.next())
			 {

				count++;

	         }

		result.close();
		stm.close();
		return count;
	}



    	public int getLastDepId() throws Exception
    	{
			int count = 0;
			Statement stm = seConnecter();
			ResultSet result = execution("SELECT * FROM depots", stm);
	     	while(result.next())
			 {

				count = result.getInt("id");

	         }

		result.close();
		stm.close();
		return count;
	}

   public int gerantPressOn(int idGerant) throws Exception
    	{
			int isActive = 0;
			Statement stm = seConnecter();
			ResultSet result = execution("SELECT isActive FROM pressing WHERE id_gerant ="+idGerant, stm);
	     	if(result.next())
			 {

				isActive = result.getInt("isActive");

	         }

		result.close();
		stm.close();
		return isActive;
	}
 
   public int empPressOn(int idemp) throws Exception
    	{
    		int id= getidPress2(idemp);
			int isActive = 0;
			Statement stm = seConnecter();
			ResultSet result = execution("SELECT isActive FROM pressing WHERE id="+id, stm);
	     	if(result.next())
			 {

				isActive = result.getInt("isActive");

	         }

		result.close();
		stm.close();
		return isActive;
	}




    public int[] login(String username, String pword) throws Exception{
		int[] resTab = new int[3];
		 
		resTab[0] = -1;
		resTab[1] = -1;
		resTab[2] = -1;


		// String pword = "notFound";
		Statement stm = seConnecter();
		ResultSet result = execution("SELECT id, username, pword FROM gerants WHERE username = '"+username+"' AND pword= '"+pword+"'", stm);
		if(result.next())
		 { 
			resTab[0] = 1 ;  resTab[1] = result.getInt("id");
            
            resTab[2] = gerantPressOn(resTab[1]);

		 }
		result.close();

		ResultSet result2 = execution("SELECT id, matricule, pword FROM employes WHERE matricule = '"+username+"' AND pword= '"+pword+"'", stm);
		if(result2.next())
		 {
		  resTab[0] = 2 ;  resTab[1] = result2.getInt("id");
           
          resTab[2] = empPressOn(resTab[1]);


	     }
		result2.close();

	    ResultSet result3 = execution("SELECT username, pword FROM admin WHERE username = '"+username+"' AND pword= '"+pword+"'", stm);
		if(result3.next()) resTab[0] = 3 ;
		result3.close();
    

		stm.close();
		return resTab;
	}


    public String getIdGerant(String username, String pword) throws Exception{
		String idGerant = "-1";
		// String pword = "notFound";
		Statement stm = seConnecter();
		ResultSet result = execution("SELECT * FROM gerants WHERE username = '"+username+"' AND pword= '"+pword+"'", stm);
		if(result.next()) idGerant = Integer.toString(result.getInt("id")) ;
		result.close();
 

		stm.close();
		return idGerant;
	}




    public int[] isCust(int idPress, int idCust) throws Exception{
		
		int[] res = new int[2];
        res[0] = 0;
        res[1] = 0;
		// String pword = "notFound";
		Statement stm = seConnecter();
		ResultSet result = execution("SELECT * FROM depots WHERE id_pressing= "+idPress+" AND id_clients= "+idCust+"", stm);

		if(result.next()) 
		{

           res[0] = 1 ;
           while(result.next())
           {

           	res[1]++;
           }

		}  
		result.close();
 

		stm.close();
		return res;
	}

    public int empDeps(int idEmp) throws Exception{
		
		int counter = 0;
		// String pword = "notFound";
		Statement stm = seConnecter();
		ResultSet result = execution("SELECT * FROM depots WHERE id_employe= "+idEmp+"", stm);
  
           while(result.next())
           {

           	counter++;
           }
 
		result.close();

		stm.close();
		return counter;
	}
   
    public int pressDeps(int idPress) throws Exception{
		
		int counter = 0;
		// String pword = "notFound";
		Statement stm = seConnecter();
		ResultSet result = execution("SELECT * FROM depots WHERE id_pressing= "+idPress+"", stm);
  
           while(result.next())
           {

           	counter++;

           }
 
		result.close();

		stm.close();
		return counter;
	}




    public int getidPress(int idGerant) throws Exception{
		
		int id = -1;
		// String pword = "notFound";
		Statement stm = seConnecter();
		ResultSet result = execution("SELECT * FROM pressing WHERE id_gerant= "+idGerant+"", stm);
  
           if(result.next())
           {

           	id = result.getInt("id_gerant");

           }
 
		result.close();

		stm.close();
		return id;
	}


    public int getidPress2(int idemp) throws Exception{
		
		int id = -1;
		// String pword = "notFound";
		Statement stm = seConnecter();
		ResultSet result = execution("SELECT * FROM employes WHERE id= "+idemp+"", stm);
  
           if(result.next())
           {

           	id = result.getInt("id_pressing");

           }
 
		result.close();

		stm.close();
		return id;
	}

  




public void newPressing(String nomPress, String username, String pword, String addr) throws Exception{
	   

		Statement stm = seConnecter();

		int result1 = updateReq("INSERT  INTO gerants(username,prenom,pword) VALUES ('"+username+"', '"+username+"', '"+pword+"')", stm);

	    
        
		int idGerant = Integer.parseInt(getIdGerant(username , pword));

		int result2 = updateReq("INSERT  INTO pressing(nom,id_gerant,Adresse) VALUES ('"+nomPress+"', "+idGerant+", '"+addr+"')", stm);


		stm.close();
	}


public void newEmp(int idpress, String prenom, String nom, String username, String pword) throws Exception{
	   

		Statement stm = seConnecter();

		int result1 = updateReq("INSERT INTO employes(id_pressing, prenom,nom,matricule,pword) VALUES ("+idpress+",'"+prenom+"', '"+nom+"', '"+username+"', '"+pword+"')", stm);

		stm.close();
	}


public void newDep(String ref, int idPress, int empId, int idCust) throws Exception{
	   

		Statement stm = seConnecter();

		int result1 = updateReq("INSERT  INTO depots(ref,id_pressing,id_employe,id_clients) VALUES ('"+ref+"', "+idPress+", "+empId+", "+idCust+")", stm);

		stm.close();
	}

public void newUser(String prenom, String nom, String addr, String numTel) throws Exception{
	   

		Statement stm = seConnecter();

		int result1 = updateReq("INSERT  INTO clients(prenom,nom,adresse,numTel) VALUES ('"+prenom+"', '"+nom+"', '"+addr+"', '"+numTel+"')", stm);

		stm.close();
	}

public void newArt(int iddep, String lib) throws Exception{
	   

		Statement stm = seConnecter();

		int result1 = updateReq("INSERT  INTO articles(id_depots,libelle) VALUES ('"+iddep+"', '"+lib+"')", stm);

		stm.close();
	}



   public void switchStatus(int idPress, int status) throws Exception{
	   

		Statement stm = seConnecter();

		int result1 = updateReq("UPDATE pressing SET isActive="+status+" WHERE id ="+idPress+"", stm);

		stm.close();
	}


   public void moveDep(int iddep, int status) throws Exception{
	   

		Statement stm = seConnecter();

		int result1 = updateReq("UPDATE depots SET estLivre="+status+" WHERE id ="+iddep+"", stm);

		stm.close();
	}



    public HashMap pressEmps(int idPress) throws Exception{
		     
		HashMap emps = new HashMap();   
        

		Statement stm = seConnecter();
		ResultSet result = execution("SELECT * FROM employes WHERE id_pressing ="+idPress+"", stm);
		int i = 0;
		while(result.next())
		{
            HashMap<String, Object> p = new HashMap<>();
            
            String mash = Integer.toString(i);  
           // pressing press = new pressing(result.getInt("id"), result.getString("nom"), result.getString("Adresse"), result.getInt("isActive") );
	        p.put("id" , result.getInt("id"));
	        p.put("prenom" , result.getString("prenom"));
	        p.put("nom" , result.getString("nom"));
	        p.put("matricule" , result.getString("matricule"));

            int deps = empDeps(result.getInt("id"));

	        p.put("deps" , deps);

            // p.put("url" , "/switcher?idPress="+result.getInt("id")+"&status="+result.getInt("isActive"));

	        emps.put(mash,p);

             i++;

		} 

		result.close();
		stm.close();

		return emps;
	}






    public HashMap pressingAll() throws Exception{
		     
		HashMap pressings = new HashMap();   
        

		Statement stm = seConnecter();
		ResultSet result = execution("SELECT * FROM pressing", stm);
		int i = 0;
		while(result.next())
		{
            HashMap<String, Object> p = new HashMap<>();
            
            String mash = Integer.toString(i);  
           // pressing press = new pressing(result.getInt("id"), result.getString("nom"), result.getString("Adresse"), result.getInt("isActive") );
	        p.put("id" , result.getInt("id"));
	        p.put("nom" , result.getString("nom"));
	        p.put("Adresse" , result.getString("Adresse"));
	        p.put("status" , result.getInt("isActive"));
            // p.put("url" , "/switcher?idPress="+result.getInt("id")+"&status="+result.getInt("isActive"));

	        pressings.put(mash,p);

             i++;

		} 

		result.close();
		stm.close();

		return pressings;
	}


    public HashMap depotsAll(int empId) throws Exception{
		     
		HashMap depots = new HashMap();   
        

		Statement stm = seConnecter();
		ResultSet result = execution("SELECT * FROM depots WHERE id_employe ="+empId, stm);
		int i = 0;

		while(result.next())
		{
            
            int idClient = result.getInt("id_clients");
		 
		    Statement stm2 = seConnecter();

		    ResultSet result2 = execution("SELECT prenom, nom ,numTel FROM clients WHERE id ="+idClient, stm2);
            
            if (result2.next())
             {
            	

		            HashMap<String, Object> d = new HashMap<>();
		            
		            String mash = Integer.toString(i);  
		           // pressing press = new pressing(result.getInt("id"), result.getString("nom"), result.getString("Adresse"), result.getInt("isActive") );
			        d.put("id" , result.getInt("id"));
			        d.put("estLivre" , result.getInt("estLivre"));
			        d.put("ref" , result.getString("ref"));
                    
			        d.put("custSname" , result2.getString("prenom"));
			        d.put("custName" , result2.getString("nom"));
			        d.put("custTel" , result2.getInt("numTel"));
		            
			        depots.put(mash,d);

		             i++;

		            result2.close();
		            stm2.close();

            }



		} 
		result.close();
		stm.close();

		return depots;
	}






   public HashMap pressCusts(int idPress) throws Exception{
		     
		HashMap custs = new HashMap();   
        

		Statement stm = seConnecter();
		ResultSet result = execution("SELECT * FROM clients", stm);
	    int i = 0;


		while(result.next())
		{
            
            int idClient = result.getInt("id");

            int[] userSpecs = new int[2];
            userSpecs = isCust(idPress , idClient) ;

		        if (userSpecs[0] != 0)
		         {
		        
                    HashMap<String, Object> d = new HashMap<>();
		            
		            String mash = Integer.toString(i);  
			        
			        d.put("id" , idClient);
			        d.put("prenom" , result.getString("prenom"));
			        d.put("nom" , result.getString("nom"));
			        d.put("numTel" , result.getInt("numTel"));
			        d.put("deps" , userSpecs[1]);
		            
			        custs.put(mash,d);

		             i++;

		         }
 

		} 
		result.close();
		stm.close();

		return custs;
	}



 


}
