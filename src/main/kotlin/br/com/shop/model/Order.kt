package br.com.shop.model

import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "order_table")
class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    var total: Double,
    @OneToMany(cascade = [CascadeType.MERGE])
    var carts: List<Cart>,
    var date: LocalDate,
)