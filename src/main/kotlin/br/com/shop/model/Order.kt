package br.com.shop.model

import br.com.shop.model.enums.OrderStatus
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "order_table")
class Order(){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0
    @OneToMany(cascade = [CascadeType.PERSIST, CascadeType.REMOVE])
    var carts: MutableList<Cart> = mutableListOf()
    @Enumerated(EnumType.STRING)
    private var status: OrderStatus = OrderStatus.ORDER_MADE
    private var date: LocalDate = LocalDate.now()
    private var total: Double = 0.0

    init {
        this.total = calculateTotal()
    }

    constructor(id: Long, carts: MutableList<Cart>) : this() {
        this.id= id
        this.carts = carts
    }

    private fun calculateTotal(): Double {
        var total = 0.0
        carts.forEach { c ->
            total += c.price * c.quantity
        }
        return total
    }

}