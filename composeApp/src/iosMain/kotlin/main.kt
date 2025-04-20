import androidx.compose.ui.window.ComposeUIViewController
import org.weather.de.app.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { App() }
