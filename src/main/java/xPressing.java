import java.util.HashMap;
import java.util.Map;
import java.sql.* ;
import spark.ModelAndView;
import java.util.ArrayList;
import spark.template.velocity.VelocityTemplateEngine;



import static spark.Spark.*;


public class xPressing
 {
     private static final String SESSION_NAME = "idGerant";
 
	  public static void main(String[] args)
	     {
 
         //Ne pas modifier    
	       staticFileLocation("/public");
         SqlQuery q = new SqlQuery();
 

        ///////////////Vue principale/////////////////
   	     get("/", (request, response) -> 
 
	       {
            //Ne pas modifier    
            Map map = new HashMap();
          

            // Ici on aura nos variables, requetes (q.xxx) et remplacement de nos $variable


            //Ne pas modifier    
            map.put("base", "templates/css.vtl");
	        	return new ModelAndView(map , "templates/home.vtl");
	       }
	       ,new VelocityTemplateEngine());


      ///////////////////////////////////////////////


 

        ///////////////Vue Inscription/////////////////
         get("/signin", (request, response) -> 
 
           {
            //Ne pas modifier    
            Map map = new HashMap();
          

            // Ici on aura nos variables, requetes (q.xxx) et remplacement de nos $variable


            //Ne pas modifier    
            map.put("base", "templates/css.vtl");
                return new ModelAndView(map , "templates/signin.vtl");
           }
           ,new VelocityTemplateEngine());


      ///////////////////////////////////////////////

        ///////////////Vue NewPressing/////////////////
         post("/newpress", (request, response) -> 
 
           {
            //Ne pas modifier    
            Map map = new HashMap();
          

            // Ici on aura nos variables, requetes (q.xxx) et remplacement de nos $variable

            String pressName = request.queryParams("pressname");
            String username = request.queryParams("username");
            String pword = request.queryParams("pword");
            String addr = request.queryParams("addr");


            q.newPressing(pressName, username, pword, addr);

            // response.redirect("/signin");

            


            //Ne pas modifier    
            map.put("base", "templates/css.vtl");
                return new ModelAndView(map , "templates/newpress.vtl");
           }
           ,new VelocityTemplateEngine());


      ///////////////////////////////////////////////



        ///////////////Vue NewEmpl/////////////////
         get("/addemp", (request, response) -> 
 
           {
            //Ne pas modifier    
            Map map = new HashMap();
    
            //Ne pas modifier    
            map.put("base", "templates/css.vtl");
                return new ModelAndView(map , "templates/addemp.vtl");
           }
           ,new VelocityTemplateEngine());


      ///////////////////////////////////////////////

       
        ///////////////Vue saveEmp/////////////////
         post("/saveemp", (request, response) -> 
 
           {
            //Ne pas modifier    
            Map map = new HashMap();
    

            String prenom = request.queryParams("firstname");
            String nom = request.queryParams("lastname");
            String username = request.queryParams("username");
            String pword = request.queryParams("pword");

            String gerantId = request.cookie(SESSION_NAME);

            int idPress = q.getidPress(Integer.parseInt(gerantId));

            boolean saved = false;

            if (!prenom.equals("") && !nom.equals("") && !username.equals("") && !pword.equals(""))
             {
                 saved = true;
                 q.newEmp(idPress, prenom , nom , username , pword);
             }

             map.put("saved" , saved);
            //Ne pas modifier    
            map.put("base", "templates/css.vtl");
                return new ModelAndView(map , "templates/saveemp.vtl");
           }
           ,new VelocityTemplateEngine());


      ///////////////////////////////////////////////

       



        ///////////////Vue Connexion/////////////////
         get("/login", (request, response) -> 
 
           {
            //Ne pas modifier    
            Map map = new HashMap();
          

            
            // Ici on aura nos variables, requetes (q.xxx) et remplacement de nos $variable
             

            //Ne pas modifier    
            map.put("base", "templates/css.vtl");
                return new ModelAndView(map , "templates/login.vtl");
           }
           ,new VelocityTemplateEngine());


      ///////////////////////////////////////////////


        ///////////////Vue RedirectLog /////////////////
         post("/redirect", (request, response) -> 
 
           {
            //Ne pas modifier    
            Map map = new HashMap();
          

            // Ici on aura nos variables, requetes (q.xxx) et remplacement de nos $variable
             
            String uname = request.queryParams("username");
            String pword = request.queryParams("pword");

            int[] userType = q.login(uname, pword);

            if (userType[0] == 1)
             {  

               String idGerant = Integer.toString(userType[1]);
               //Setting the g id val
               response.cookie(SESSION_NAME, idGerant);
               int isActice = userType[2];
               if (isActice==1)
                {
                  response.redirect("/geranthome");
                } 
                else
                {
                  response.redirect("/inactive");
                }

            

             }
            if (userType[0] == 2)
             {

               String idEmp = Integer.toString(userType[1]);
               //Setting the g id val
               response.cookie(SESSION_NAME, idEmp);
               response.redirect("/mainempl"); 

               int isActice = userType[2];
               if (isActice==1)
                {
                  response.redirect("/geranthome");
                } 
                else
                {
                  response.redirect("/inactive");
                }

             }
            if (userType[0] == 3)
             {
               response.redirect("/admin");   
             }

            if (userType[0] == -1)
             {
               response.redirect("/login");   
             }


            //Ne pas modifier    
            // map.put("base", "templates/css.vtl");
                return new ModelAndView(map , "templates/redirect.vtl");
           }
           ,new VelocityTemplateEngine());


        ///////////////////////////////////////////////

        /////////////// Inactive view ////////////////

         get("/inactive", (request, response) -> 
 
           {
            //Ne pas modifier    
            Map map = new HashMap();
          

            
            // Ici on aura nos variables, requetes (q.xxx) et remplacement de nos $variable
             

            //Ne pas modifier    
            map.put("base", "templates/css.vtl");
                return new ModelAndView(map , "templates/inactive.vtl");
           }
           ,new VelocityTemplateEngine());

        ////////////////////////////////////////////////

        ///////////////Vue Gerant/////////////////
         get("/geranthome", (request, response) -> 
 
           {
            //Ne pas modifier    
            Map map = new HashMap();
          

            // Ici on aura nos variables, requetes (q.xxx) et remplacement de nos $variable
           
                
                String gerantId = request.cookie(SESSION_NAME);
                int idPress = q.getidPress(Integer.parseInt(gerantId));
                 
                int pressDeps = q.pressDeps(idPress);

                HashMap emps = q.pressEmps(idPress);

                HashMap custs = q.pressCusts(idPress);
                 
                map.put("alldeps" ,pressDeps);
                map.put("emps" ,emps);
                map.put("custs" ,custs);

 

                // map.put("who" ,who);

                //Ne pas modifier    
                map.put("base", "templates/css.vtl");
                return new ModelAndView(map , "templates/geranthome.vtl");
           }
           ,new VelocityTemplateEngine());


      ///////////////////////////////////////////////



        ///////////////Vue Gerant/////////////////
         get("/empl", (request, response) -> 
 
           {
               //Ne pas modifier    
               Map map = new HashMap();
          

            // Ici on aura nos variables, requetes (q.xxx) et remplacement de nos $variable
            
                String empId = request.cookie(SESSION_NAME);

                 HashMap depots =  q.depotsAll(Integer.parseInt(empId));

                 map.put("depots" , depots);

                //Ne pas modifier    
                map.put("base", "templates/css.vtl");
                return new ModelAndView(map , "templates/empdeps.vtl");
           }
           ,new VelocityTemplateEngine());


        ///////////////////////////////////////////////

        ///////////////Vue Gerant/////////////////
         get("/mainempl", (request, response) -> 
 
           {
            //Ne pas modifier    
            Map map = new HashMap();
          

            // Ici on aura nos variables, requetes (q.xxx) et remplacement de nos $variable
           
                
                String empId = request.cookie(SESSION_NAME);
                int idPress = q.getidPress2(Integer.parseInt(empId));
                 

                HashMap custs = q.pressCusts(idPress);
               
                map.put("custs" ,custs);

 

                // map.put("who" ,who);

                //Ne pas modifier    
                map.put("base", "templates/css.vtl");
                return new ModelAndView(map , "templates/setdepot.vtl");
           }
           ,new VelocityTemplateEngine());


      ///////////////////////////////////////////////



        
      ////////////////// savedep view ///////////////////  
         post("/savedep", (request, response) -> 
 
           {
               //Ne pas modifier    
               Map map = new HashMap();
          

            // Ici on aura nos variables, requetes (q.xxx) et remplacement de nos $variable

                String lib1 = request.queryParams("art0");

                boolean saved = false;

            if (!lib1.equals(""))
             {
                  saved = true;

            
                String empId = request.cookie(SESSION_NAME);
                
                String userType = request.queryParams("neworlist");
                
                // map.put("type" , userType);
                
                    int idPress = q.getidPress2(Integer.parseInt(empId));

                    int userId = 0;
                    if (userType.equals("new"))
                     {
                        String prenom = request.queryParams("custsname"); 
                        String nom = request.queryParams("custname"); 
                        String adresse = request.queryParams("custaddr"); 
                        String numtel = request.queryParams("custnum"); 
                        
                        if (prenom.equals("")) {prenom ="undefined" ;} 
                        if (nom.equals("")) {nom ="undefined" ;} 
                        if (adresse.equals("")) {adresse ="undefined"; } 
                        if (numtel.equals("")) {numtel ="0"; } 

                        q.newUser(prenom, nom, adresse, numtel);
                        
                        userId = q.getLastUId();


                     }
                     else
                     { userId = Integer.parseInt(request.queryParams("selectuser")); }

                     int depCount = q.depotsAll2(); //Getting the deps count
                     
                     //Setting the ref 
                     String ref ="#XP"+idPress+empId+"AQ"+userId+depCount;
                     //Save the dep
                     q.newDep(ref, idPress,Integer.parseInt(empId), userId);

                     int depId = q.getLastDepId(); 
                     

                      String artCount = request.queryParams("artCount");
                      // map.put("type" , prenom); 

                      for (int i=0; i<Integer.parseInt(artCount); i++) 
                      {
                          
                         String lib = request.queryParams("art"+i);

                         q.newArt(depId , lib);

                      }

                    map.put("ref" , ref) ;


                 }//Saved

                    map.put("saved" , saved) ;
                




                map.put("base", "templates/css.vtl");
                
                return new ModelAndView(map , "templates/savedep.vtl");
           }
           ,new VelocityTemplateEngine());

         //////////////////////////////////////////////////



        ///////////////Vue Movedep/////////////////
         get("/movedep", (request, response) -> 
 
           {
               //Ne pas modifier    
                Map map = new HashMap();
          

                 // Ici on aura nos variables, requetes (q.xxx) et remplacement de nos $variable
            
                 String depId = request.queryParams("iddep");
                 String status = request.queryParams("status");

                 q.moveDep(Integer.parseInt(depId) , Integer.parseInt(status));
                 response.redirect("/empl");
                //Ne pas modifier    
                map.put("base", "templates/css.vtl");
                return new ModelAndView(map , "templates/movedep.vtl");
           }
           ,new VelocityTemplateEngine());


      ///////////////////////////////////////////////





        ///////////////Vue Admin/////////////////
         get("/admin", (request, response) -> 
 
           {
            //Ne pas modifier    
            Map map = new HashMap();
          

            // Ici on aura nos variables, requetes (q.xxx) et remplacement de nos $variable
            HashMap presss =  q.pressingAll();

                map.put("pressings" , presss);

                //Ne pas modifier    
                map.put("base", "templates/css.vtl");
                return new ModelAndView(map , "templates/adminhome.vtl");
           }
           ,new VelocityTemplateEngine());


      ///////////////////////////////////////////////

     ///////////////Vue switchStatus/////////////////
         get("/switcher", (request, response) -> 
 
           {
            //Ne pas modifier    
            Map map = new HashMap();
          

            // Ici on aura nos variables, requetes (q.xxx) et remplacement de nos $variable
           
            int idPress = Integer.parseInt(request.queryParams("idPress"));
            int newStatus = Integer.parseInt(request.queryParams("status"));

            q.switchStatus(idPress, newStatus);

                //Ne pas modifier    
                response.redirect("/admin");
                map.put("base", "templates/css.vtl");
                return new ModelAndView(map , "templates/switcher.vtl");
           }
           ,new VelocityTemplateEngine());


      ///////////////////////////////////////////////



    
        }
 
 }