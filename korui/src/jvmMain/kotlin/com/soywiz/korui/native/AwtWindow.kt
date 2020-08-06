package com.soywiz.korui.native

import com.soywiz.korev.*
import com.soywiz.korio.lang.*
import com.soywiz.korma.geom.*
import com.soywiz.korui.*
import com.soywiz.korui.native.util.*
import java.awt.Rectangle
import java.awt.event.*
import javax.swing.*

open class AwtWindow(factory: AwtUiFactory, val frame: JFrame = JFrame()) : AwtContainer(factory, frame, frame.contentPane), NativeUiFactory.NativeWindow {
    init {
        frame.contentPane.layout = null
        frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        frame.setLocationRelativeTo(null)
    }

    override var bounds: RectangleInt
        get() {
            val b = frame.contentPane.bounds
            return RectangleInt(b.x, b.y, b.width, b.height)
        }
        set(value) {
            frame.contentPane.bounds = Rectangle(value.x, value.y, value.width, value.height)
            frame.bounds = Rectangle(value.x, value.y, value.width, value.height)
        }

    override var visible: Boolean
        get() = super<AwtContainer>.visible
        set(value) {
            super<AwtContainer>.visible = value
            frame.setLocationRelativeTo(null)
        }
    override var title: String
        get() = frame.title
        set(value) {
            frame.title = value
        }

    override var menu: UiMenu? = null
        set(value) {
            field = value
            frame.jMenuBar = value?.toJMenuBar()
        }

    override var focusable: Boolean
        get() = frame.contentPane.isFocusable
        set(value) {
            frame.contentPane.isFocusable = value
        }

    override fun onResize(handler: (ReshapeEvent) -> Unit): Disposable {
        val listener = object : ComponentAdapter() {
            override fun componentResized(e: ComponentEvent) {
                handler(ReshapeEvent(frame.x, frame.y, frame.contentPane.width, frame.contentPane.height))
            }
        }
        frame.addComponentListener(listener)
        return Disposable {
            frame.removeComponentListener(listener)
        }
    }
}