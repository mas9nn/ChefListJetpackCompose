package nashemenu.kz.data.db.entities.funcitonsModel

enum class FunctionsEnum(var type:String,var typeDec:Int) {
    qrFunction("qr",1),
    paymentFunction("payment",-1),
    ordersFunction("orders",2),
    notificationsFunction("notifications",-1),
    searchFunction("search",-1),
    achievementsFunction("achievements",3),
    giftsFunction("gifts",-1),
    savesFunction("saves",5),
    settingsFunction("settings",6),
    profileFunction("profile",-1),
}