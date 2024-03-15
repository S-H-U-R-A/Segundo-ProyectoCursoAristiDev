package com.example.jetpackcomposecatalogo

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutBaseScope
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import androidx.core.net.toUri
import com.example.jetpackcomposecatalogo.ui.theme.JetPackComposeCatalogoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetPackComposeCatalogoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                        Button(
                            onClick = {
                                val deepLinkIntent = Intent(
                                    Intent.ACTION_VIEW,
                                    "myapptest.com://home/Te amo muchisimo mi princesa precisosa".toUri()
                                )
                                val deepLinkPendingIntent: PendingIntent? = TaskStackBuilder.create(applicationContext).run {
                                    this.addNextIntentWithParentStack(deepLinkIntent)
                                    this.getPendingIntent(
                                        0,
                                        PendingIntent.FLAG_UPDATE_CURRENT.or(PendingIntent.FLAG_IMMUTABLE)
                                    )
                                }
                                deepLinkPendingIntent?.send()
                            }
                        ) {
                            Text(text = "Ir a la otra app")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MyStateExample(){

    /*
        var counter: Int by remember {
            mutableStateOf(0)
        }
    */

    var counter: Int by rememberSaveable {
        mutableStateOf(0)
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Button(
            onClick = {
                counter++
            }
        ) {
            Text(text = "Pulsar")
        }
        Text(text = "He sido pulsado $counter veces")
    }
}


@Composable
fun ConstraintExampleDecoupled(){
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        
        val constraints: ConstraintSet = if (minWidth < 600.dp) {
            decoupledConstraints(margin = 16.dp) //Si el ancho es menor a 600dp se le asigna un margen top de 16dp
        } else {
            decoupledConstraints(margin = 32.dp) //Si el ancho es mayor a 600dp se le asigna un margen top de 32dp
        }

        ConstraintLayout(constraints) { //Acá de pasan las restricciones creadas anteriormente
            Button(
                onClick = { /* Do something */ },
                modifier = Modifier.layoutId("button")// Se asigna por decirlo de alguna forma un id, para que se pueda referenciar en las restricciones
            ) {
                Text("Button")
            }

            Text("Text", Modifier.layoutId("text"))// Se asigna por decirlo de alguna forma un id, para que se pueda referenciar en las restricciones
        }
    }
}

/**
 * Decoupled constraints
 *
 * Es un método auxiliar de ejemplo que retorna un [ConstraintSet] con un margen dado
 *
 * @param margin de tipo Dp que representa los margenes que se le van a dar a los composables
 * @return [ConstraintSet] que representa las restricciones que se le van a dar a los composables
 */
private fun decoupledConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet { //Constructor de ConstraintSet

        val button = createRefFor("button") //Creamos una referencia para un boton
        val text = createRefFor("text") //Creamos una referencia para una caja de texto

        constrain(button) {//Asignamos la referencia y creamos las restricciones para el botón, además asignamos su margen
            top.linkTo(parent.top, margin = margin)
        }

        constrain(text) {//Asignamos la referencia y creamos las restricciones para la caja de Texto, además asignamos su margen
            top.linkTo(button.bottom, margin)
        }
    }
}

@Composable
fun ConstraintExampleChain(){
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ){

        val (boxRed, boxGreen, boxYellow) = createRefs()

        //Definimos la cadena
        createHorizontalChain(
            boxRed,
            boxGreen,
            boxYellow,
            chainStyle = ChainStyle.SpreadInside
        )

        Box(
            modifier = Modifier
                .size(75.dp)
                .background(Color.Green)
                .constrainAs(boxGreen) {
                    start.linkTo(anchor = parent.start)
                    end.linkTo(anchor = boxRed.start)
                }
        )

        Box(
            modifier = Modifier
                .size(75.dp)
                .background(Color.Red)
                .constrainAs(boxRed) {
                    start.linkTo(anchor = boxGreen.end)
                    end.linkTo(anchor = boxYellow.start)
                }
        )

        Box(
            modifier = Modifier
                .size(75.dp)
                .background(Color.Yellow)
                .constrainAs(boxYellow) {
                    start.linkTo(anchor = boxRed.end)
                    end.linkTo(anchor = parent.end)
                }
        )

    }
}

@Composable
fun ConstraintExampleBarrier(){
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {//Todo lo que tenga que ver con ConstraintLayout debe estar dentro de la lambda del COmposable

        val (boxRed, boxGreen, boxYellow) = createRefs()

        //La barrera agrupa composables en este caso por el final de los composables
        //Y la idea es que sea como un muro en el cual los otros composables no puedan
        //pasar  la barrera y que se choquen o se solapen los otros composables

        //En otras palabras la linea de la barrera siempre se va a ajustar con respecto
        //a la orientación de los composables que se indican que forman
        //el grupo de la barrera
        val endBarrier = createEndBarrier(boxRed, boxGreen)

        Box(
            modifier = Modifier
                .size(125.dp)
                .background(Color.Green)
                .constrainAs(boxGreen) {
                    start.linkTo(anchor = parent.start, margin = 16.dp)
                }
        )

        Box(
            modifier = Modifier
                .size(200.dp)
                .background(Color.Red)
                .constrainAs(boxRed) {
                    top.linkTo(anchor = boxGreen.bottom)
                    start.linkTo(anchor = parent.start, margin = 32.dp)
                }
        )

        Box(
            modifier = Modifier
                .size(35.dp)
                .background(Color.Yellow)
                .constrainAs(boxYellow) {
                    start.linkTo(endBarrier)
                }
        )

    }
}

@Composable
fun ConstraintExampleGuideline(){
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {

        val boxRed: ConstrainedLayoutReference = createRef()

        //Las guias son como lineas invisibles que me permiten enganchar
        //los composables a ellas, en este ejemplo queremos que la caja
        //siempre se ubique en el 10% del alto de la pantalla y en el 25%
        //del ancho de la pantalla
        val topGuideLine: ConstraintLayoutBaseScope.HorizontalAnchor =
            createGuidelineFromTop(0.1f)

        val startGuideLine: ConstraintLayoutBaseScope.VerticalAnchor =
            createGuidelineFromStart(0.25f)

        Box(
            modifier = Modifier
                .size(125.dp)
                .background(Color.Red)
                .constrainAs(boxRed) {
                    top.linkTo(topGuideLine)
                    start.linkTo(startGuideLine)
                }
        )

    }
}

@Composable
fun ConstraintExampleBasic(){

    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {

        val (boxRed, boxBlue, boxYellow, boxMagenta, boxGreen) = createRefs()

        Box(
            modifier = Modifier
                .size(125.dp)
                .background(Color.Red)
                .constrainAs(boxRed) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }
        ){
            Text(
                text = "Box Red Box red Box red Box red Box red Box Box Red Box red Box red Box red Box red Box Box Red Box red Box red Box red Box red Box Box Red Box red Box red Box red Box red Box Box",
                color = Color.White,
                textAlign = TextAlign.Center,
                softWrap = false,
                modifier = Modifier
                    .align(Alignment.Center)
                    .horizontalScroll(
                        state = rememberScrollState()
                    )
            )
        }

        Box(
            modifier = Modifier
                .size(125.dp)
                .background(Color.Blue)
                .constrainAs(boxBlue) {
                    top.linkTo(boxRed.bottom)
                    start.linkTo(boxRed.end)
                }
        )

        Box(
            modifier = Modifier
                .size(125.dp)
                .background(Color.Green)
                .constrainAs(boxGreen) {
                    top.linkTo(boxRed.bottom)
                    end.linkTo(boxRed.start)
                }
        )

        Box(
            modifier = Modifier
                .size(125.dp)
                .background(Color.Yellow)
                .constrainAs(boxYellow) {
                    bottom.linkTo(boxRed.top)
                    end.linkTo(boxRed.start)
                }
        )

        Box(
            modifier = Modifier
                .size(125.dp)
                .background(Color.Magenta)
                .constrainAs(boxMagenta) {
                    bottom.linkTo(boxRed.top)
                    start.linkTo(boxRed.end)
                }
        )
    }

}

@Composable
fun MyComplexLayout() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.Cyan)
        ) {
            Text(
                text = "Ejemplo 1",
                modifier = Modifier.align(Alignment.Center) //Esta forma de alinear aplica para composables individualmente
            )
        }

        MySpacerVertical(height = 30)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Color.Red)
            ) {
                Text(
                    text = "Ejemplo 2",
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Box(
                contentAlignment = Alignment.Center, //Esta forma de alinear indica la posición de todos los composables secundarios
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Color.Green)
            ) {
                Text(
                    text = "Hola Soy Sergio Andrés",
                    color = Color.Blue
                )
            }
        }

        MySpacerVertical(height = 80)

        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.Magenta)
        ) {
            Text(
                text = "Ejemplo 4"
            )
        }
    }
}

@Composable
fun MySpacerVertical( height : Int){
    Spacer(
        modifier = Modifier.height( height = height.dp )
    )
}

@Composable
fun MyRow() {
    /*    Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .horizontalScroll(state = rememberScrollState() )

        ){
            Text(
                text = "Hola Mundo 1"
            )
            Text(
                text = "Hola Mundo 2"
            )
            Text(
                text = "Hola Mundo 3"
            )
        }*/
    Row(
        modifier = Modifier
            .fillMaxSize()
            .horizontalScroll(state = rememberScrollState())
    ) {
        Text(
            text = "Hola Mundo 1",
            softWrap = false,
            modifier = Modifier
                //.weight(1f)
                .width(100.dp)
                .border(width = 1.dp, color = Color.DarkGray, shape = RoundedCornerShape(5.dp))
        )
        Text(
            text = "Hola Mundo 2",
            softWrap = false,
            modifier = Modifier
                //.weight(1f)
                .width(100.dp)
        )
        Text(
            text = "Hola Mundo 3",
            softWrap = false,
            modifier = Modifier
                //.weight(1f)
                .width(100.dp)
        )
        Text(
            text = "Hola Mundo 4",
            softWrap = false,
            modifier = Modifier
                //.weight(1f)
                .width(100.dp)
        )
        Text(
            text = "Hola Mundo 5",
            softWrap = false,
            modifier = Modifier
                //.weight(1f)
                .width(100.dp)
        )
    }
}

@Composable
fun MyColumn() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Ejemplo 1",
            modifier = Modifier.background(Color.Red)
        )
        Text(
            text = "Ejemplo lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum final ",
            color = Color.White,
            textAlign = TextAlign.Center,
            textDecoration = TextDecoration.Underline,
            softWrap = true,
            maxLines = 3,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .align(alignment = Alignment.End)
        )
        Text(
            text = "Ejemplo 3",
            modifier = Modifier.background(Color.Cyan)
        )
        Text(
            text = "Ejemplo 4",
            modifier = Modifier.background(Color.Blue)
        )
        Spacer(modifier = Modifier.height(50.dp))
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            alignment = Alignment.Center,
            contentScale = ContentScale.Crop,
            alpha = 1F,
            colorFilter = ColorFilter.tint(Color.Cyan, BlendMode.ColorBurn),
            contentDescription = null,
            modifier = Modifier
                .size(width = 150.dp, height = 150.dp)
                .align(Alignment.CenterHorizontally),
        )
    }

}

@Composable
fun MyBox() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(200.dp)
                .height(200.dp)
                .background(Color.Cyan)
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(text = "Esto es un ejemplo")
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun DefaultPreview() {
    JetPackComposeCatalogoTheme {
        MyStateExample()
    }
}