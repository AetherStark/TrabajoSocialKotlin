<?php /*  * El siguiente código localiza un alumno  * AGZ    3/11/2016  */ 
 
$response = array(); 
 
$Cn = mysqli_connect("localhost","root","","tsocial")or die ("server no encontrado"); 
mysqli_set_charset($Cn,"utf8"); 
 
// Checa que le este llegando por el método GET el nocontrol 
if ($_SERVER['REQUEST_METHOD']=='POST') {  

    $result = mysqli_query($Cn,"SELECT idtrabajador,nombre,correo,domicilio,telefono,password from trabajadores"); 
 
    if (!empty($result)) {         
        if (mysqli_num_rows($result) > 0) { 
            $trabajador = array(); 
            $response["success"] = 200;   
            $response["message"] = 'trabajadores encontrados';   
            $response["trabajadores"] = array(); 

            while($res = mysqli_fetch_array($result)){

                $trabajador["idtrabajador"] = $res["idtrabajador"];       
                $trabajador["nombre"] = $res["nombre"]; 
                $trabajador["correo"] = $res["correo"]; 
                $trabajador["domicilio"] = $res["domicilio"]; 
                $trabajador["telefono"] = $res["telefono"];
                $trabajador["password"] = $res["password"]; 
                array_push($response["trabajadores"], $trabajador);

            }             
            
 
            // El success=10 es que encontro el producto          

 
           // codifica la información en formato de JSON response            
           echo json_encode($response);         } 
           else {             
            // No Encontro al alumno             
            $response["success"] = 404;  
            //No encontro información y el success = 0 indica no exitoso             
            $response["message"] = "Trabajadores no encontrados";             
            echo json_encode($response);         }     } 
            else {         
                $response["success"] = 404;  //No encontro información y el success = 0 indica no exitoso         
                $response["message"] = "Trabajadores no encontrados no encontrados";         
                echo json_encode($response);     } } 
                else {     
                    // required field is missing     
                    $response["success"] = 400;     
                    $response["message"] = "Faltan Datos de entrada"; 
    
                    echo json_encode($response); 
}
mysqli_close($Cn); 
?> 