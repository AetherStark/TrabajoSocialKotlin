<?php /*  * El siguiente código localiza un alumno  * AGZ    3/11/2016  */ 
 
$response = array(); 
 
$Cn = mysqli_connect("localhost","root","","tsocial")or die ("server no encontrado"); 
mysqli_set_charset($Cn,"utf8"); 
 
// Checa que le este llegando por el método GET el nocontrol 
if ($_SERVER['REQUEST_METHOD']=='POST') {  
    
   
    $result = mysqli_query($Cn,"SELECT idescuela,nombre,direccion,telefono,idtrabajador from escuela order by nombre"); 
 
    if (!empty($result)) {         
        if (mysqli_num_rows($result) > 0) { 
            $escuela = array(); 
            $response["success"] = 200;   
            $response["message"] = 'escuelas encontradas';   
            $response["escuelas"] = array(); 

            while($res = mysqli_fetch_array($result)){

                $escuela["idescuela"] = $res["idescuela"];
                $escuela["nombre"] = $res["nombre"];       
                $escuela["direccion"] = $res["direccion"]; 
                $escuela["telefono"] = $res["telefono"];   
                $escuela["idtrabajador"] = $res["idtrabajador"];      
                
                array_push($response["escuelas"], $escuela);

            }             
            
 
            // El success=10 es que encontro el escuela          

 
           // codifica la información en formato de JSON response            
           echo json_encode($response);         } 
           else {             
            // No Encontro al alumno             
            $response["success"] = 404;  
            //No encontro información y el success = 0 indica no exitoso             
            $response["message"] = "escuela no encontrado";             
            echo json_encode($response);         }     } 
            else {         
                $response["success"] = 404;  //No encontro información y el success = 0 indica no exitoso         
                $response["message"] = "escuela no encontrado no encontrado";         
                echo json_encode($response);     } } 
                else {     
                    // required field is missing     
                    $response["success"] = 400;     
                    $response["message"] = "Faltan Datos de entrada"; 
    
                    echo json_encode($response); 
}
mysqli_close($Cn); 
?> 