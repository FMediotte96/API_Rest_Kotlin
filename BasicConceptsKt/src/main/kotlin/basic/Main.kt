package basic

fun main() {
    println("Hello World")

    val product = Product("Apple", 22.2)
    val product1 = Product(name = "Apple", price = 22.3)

    println(product)
    println("Name: ${product.name}")

    println(product1)
    println("Name: ${product1.name}")

    println("product == product 1: ${product == product1}")

//    println("price: ${product1.price}")
//    product1.price = 33.3
//    println("price: ${product1.price}")
}

//Val -> Inmutable
//Var -> Mutable
data class Product(val name: String, var price: Double? = 55.5) {
    override fun equals(other: Any?): Boolean {
        other ?: return false
        if(other === this) return true
        if(this.javaClass != other.javaClass) return false
        other as Product

        return this.name == other.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

}