package br.com.shop.model

import br.com.shop.model.enums.OrderStatus
import java.time.LocalDate
import jakarta.persistence.*

@Entity
@Table(name = "order_table")
class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @OneToMany(cascade = [CascadeType.PERSIST, CascadeType.REMOVE])
    val carts: MutableList<Cart> = mutableListOf(),
    @Enumerated(EnumType.STRING)
    private var status: OrderStatus = OrderStatus.ORDER_MADE,
    private var date: LocalDate = LocalDate.now(),
    private var total: Double = 0.0,

) {

    init {
        this.total = calculateTotal()
    }
    private fun calculateTotal(): Double {
        var total = 0.0
        carts.forEach { c ->
            total += c.price * c.quantity
        }
        return total
    }

}