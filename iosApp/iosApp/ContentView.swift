import UIKit
import SwiftUI
import ComposeApp


struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController(swiftToKotlinInitializerBlock: { () -> Void in
            NativeViewFactoryKt.setNativeViewFactory(factory: SwiftNativeViewFactory.shared)
        })
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    }
}

struct ContentView: View {
    var body: some View {
        ComposeView()
            .ignoresSafeArea()
    }
}



