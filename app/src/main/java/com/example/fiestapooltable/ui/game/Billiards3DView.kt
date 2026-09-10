package com.example.fiestapooltable.ui.game

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import io.github.sceneview.Scene
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberEngine

@Composable
fun Billiards3DView(
    modifier: Modifier = Modifier
) {
    val engine = rememberEngine()
    val context = LocalContext.current
    val modelLoader = ModelLoader(engine, context)
    val modelInstance = modelLoader.createModelInstance("pool_table_animation.glb")

    if (modelInstance != null) {
        Scene(
            modifier = modifier.fillMaxSize(),
            engine = engine,
            childNodes = listOf(
                ModelNode(
                    modelInstance = modelInstance,
                    scaleToUnits = 2.5f
                )
            )
        )
    }
}
