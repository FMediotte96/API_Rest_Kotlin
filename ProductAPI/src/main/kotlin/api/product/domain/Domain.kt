package api.product.domain

import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.Min
import javax.validation.constraints.Size

@Entity
data class Product(
    @Id
    @get:Size(min = 3, max = 20)
    val name: String,
    @get:Min(0)
    var price: Double? = 55.5,
    @get:Min(0)
    var stock: Int = 0,
    @ManyToOne
    val provider: Provider
) {
    override fun equals(other: Any?): Boolean {
        other ?: return false
        if (other === this) return true
        if (this.javaClass != other.javaClass) return false
        other as Product

        return this.name == other.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

}

@Entity
data class Provider(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    @get:Size(min = 3, max = 20)
    val name: String,
    @get:Email
    val email: String
) {
    override fun equals(other: Any?): Boolean {
        other ?: return false
        if (other === this) return true
        if (this.javaClass != other.javaClass) return false
        other as Provider

        return this.id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}