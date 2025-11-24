import UIKit
import SwiftUI
import ComposeApp

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController(initBlock: { () -> Void in
            let nativeFactory: NativeViewFactory = SwiftNativeViewFactory()
            Platform_iosKt.initializeComposeApp(factory: nativeFactory)
        })
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        ComposeView()
            .ignoresSafeArea()
    }
}



