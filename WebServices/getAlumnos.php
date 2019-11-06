<?php /*  * El siguiente código localiza un alumno  * AGZ    3/11/2016  */ 
 
$response = array(); 
 
$Cn = mysqli_connect("localhost","root","","tsocial")or die ("server no encontrado"); 
mysqli_set_charset($Cn,"utf8"); 
 
// Checa que le este llegando por el método GET el nocontrol 
if ($_SERVER['REQUEST_METHOD']=='POST') {  
    
    $obj= json_decode(file_get_contents("php://input"),true);
    
    $idEscuela = $obj['idescuela'];
    $result = mysqli_query($Cn,"SELECT nombre,edad,grado,idescuela from alumnos where idescuela=$idEscuela order by nombre"); 
 
    if (!empty($result)) {         
        if (mysqli_num_rows($result) > 0) { 
            $alumno = array(); 
            $response["success"] = 200;   
            $response["message"] = 'alumnos encontrados';   
            $response["alumnos"] = array(); 
            while($res = mysqli_fetch_array($result)){

                $alumno["nombre"] = $res["nombre"];
                $alumno["edad"] = $res["edad"];        
                $alumno["grado"] = $res["grado"];
                $alumno["idescuela"] = $res["idescuela"];        
                
                array_push($response["alumnos"], $alumno);

            }             
            
 
            // El success=10 es que encontro el escuela          

 
           // codifica la información en formato de JSON response            
           echo json_encode($response);         } 
           else {             
            // No Encontro al alumno             
            $response["success"] = 404;  
            //No encontro información y el success = 0 indica no exitoso             
            $response["message"] = "alumnos no encontrados";             
            echo json_encode($response);         }     } 
            else {         
                $response["success"] = 404;  //No encontro información y el success = 0 indica no exitoso         
                $response["message"] = "alumnos no encontrados no encontrados";         
                echo json_encode($response);     } } 
                else {     
                    // required field is missing     
                    $response["success"] = 400;     
                    $response["message"] = "Faltan Datos de entrada"; 
    
                    echo json_encode($response); 
}
mysqli_close($Cn); 
?> 