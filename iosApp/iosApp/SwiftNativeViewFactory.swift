//
// Created by Savo Bozic on 24. 11. 2025..
//

import Foundation
import SwiftUI
import ComposeApp

class SwiftNativeViewFactory: NativeViewFactory {
    func createNativeView(label: String, onClick: @escaping () -> Void) -> UIViewController {
        let button = SwiftUIButton(label: label, onClick: onClick)
        return UIHostingController(rootView: button)
    }
}