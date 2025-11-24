//
// Created by Savo Bozic on 23. 11. 2025..
//

import Foundation
import SwiftUI

struct SwiftUIButton: View {
    let label: String
    let onClick: () -> Void

    init(label: String, onClick: @escaping () -> Void) {
        self.label = label
        self.onClick = onClick
    }

    var body: some View {
        Button(action: onClick) {
            Text(label)
        }
        .buttonStyle(.automatic)
    }
}
