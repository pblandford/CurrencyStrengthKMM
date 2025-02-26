import SwiftUI
import ComposeApp
import FirebaseCore
import FirebaseMessaging

extension Notification.Name {
    static let didReceiveFCMToken = Notification.Name("didReceiveFCMToken")
    static let didReceiveForegroundNotification = Notification.Name("didReceiveForegroundNotification")
}

class AppDelegate: NSObject, UIApplicationDelegate, UNUserNotificationCenterDelegate, MessagingDelegate {
    
    private let dispatchGroup = DispatchGroup()
    private var fcmToken: String?
    
    
    func application(_ application: UIApplication,
                     didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {

        print("launchOptions \(launchOptions)")
        UNUserNotificationCenter.current().delegate = self

        dispatchGroup.enter()
        dispatchGroup.enter()
        
        //By default showPushNotification value is true.
        //When set showPushNotification to false foreground push  notification will not be shown.
        //You can still get notification content using #onPushNotification listener method.
        NotifierManager.shared.initialize(configuration: NotificationPlatformConfigurationIos(
            showPushNotification: true,
            askNotificationPermissionOnStart: true,
            notificationSoundName: nil)
        )
        
        Messaging.messaging().token { token, error in
            if let token = token {
                print("Received FCM token from Firebase: \(token)")
                self.fcmToken = token
            } else {
                print("Failed to get FCM token: \(error?.localizedDescription ?? "Unknown error")")
            }
          //  self.dispatchGroup.leave() // FCM token received or failed
        }
        

        UNUserNotificationCenter.current().getNotificationSettings { settings in
            print("🔍 Notification Authorization Status: \(settings.authorizationStatus.rawValue)")
        }

        dispatchGroup.notify(queue: .main) {
            NotificationCenter.default.post(name: .didReceiveFCMToken, object: self.fcmToken)
        }
        
   if let composeResourcesPath = Bundle.main.path(forResource: "compose-resources", ofType: nil) {
       do {
           let files = try FileManager.default.contentsOfDirectory(atPath: composeResourcesPath)
           print("Files in compose-resources: \(files)")
       } catch {
           print("Error listing compose-resources files: \(error)")
       }
   } else {
       print("compose-resources directory not found")
   }
        
        return true
    }
    
   
    func userNotificationCenter(_ center: UNUserNotificationCenter,
                                 willPresent notification: UNNotification,
                                 withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
         print("Foreground push received: \(notification.request.content.userInfo)")
          dispatchGroup.notify(queue: .main) {
                     NotificationCenter.default.post(name: .didReceiveForegroundNotification, object: nil)
          }

         // Show banner and play sound
         completionHandler([.banner, .sound])
     }

     func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable: Any],
      fetchCompletionHandler completionHandler:  (UIBackgroundFetchResult)->Void) {
        print("ios: didReceiveRemoteNotification")
           dispatchGroup.notify(queue: .main) {
                 NotificationCenter.default.post(name: .didReceiveForegroundNotification, object: nil)
            }
        completionHandler(.newData)
    }

     func userNotificationCenter(_ center: UNUserNotificationCenter,
                                 didReceive response: UNNotificationResponse,
                                 withCompletionHandler completionHandler: @escaping () -> Void) {
         print("Background notification received")
         let userInfo = response.notification.request.content.userInfo
         print("period \(userInfo["period"])")
         completionHandler()
     }
    
    func userNotificationCenter(_ center: UNUserNotificationCenter,
                                didFailToRegisterForRemoteNotificationsWithError error: Error) {
        
        print("UNUserNotificationCenter failed: \(error.localizedDescription)")
    }

    //
    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        print("XYZ Device token: \(deviceToken.map { String(format: "%02.2hhx", $0) }.joined())")
        
        Messaging.messaging().apnsToken = deviceToken
    }

    func application(_ application: UIApplication,
                     didFailToRegisterForRemoteNotificationsWithError
                     error: Error) {
        print(String(format: "XYZ Error failed register %s", error.localizedDescription))
    }
    
}

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate
    
    init() {
        FirebaseApp.configure()
        InitHelperKt.doInitKoin()
        InitHelperKt.doInitNotifications()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
