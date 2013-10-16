<!DOCTYPE html>
<html>
    <head>
        <title>Uploader</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="css/bootstrap.min.css" rel="stylesheet">

    </head>
    <body onload="">
        <div class="container">
            <div id="menu">

            </div>
            <div id="canvas">

            </div>
        </div>
        <script src="js/jquery-2.0.3.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <script src="js/waitUntilExists.js"></script>
        <script>
            $("#menu").load("plantillas/menu.html");
            waitUntilExists("search", function() {
                $("#search").remove();
            });
        </script>
    </body>
</html>