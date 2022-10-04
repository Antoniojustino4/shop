package br.com.shop.model

import br.com.shop.model.user.Address
import br.com.shop.model.user.Card
import javax.persistence.*

@Entity
class ShopUser(
    var name: String,
    @OneToMany(cascade = [CascadeType.PERSIST, CascadeType.REMOVE])
    var orderList: MutableList<Order> = mutableListOf(),
    @OneToMany(cascade = [CascadeType.PERSIST, CascadeType.REMOVE])
    var addressList: MutableList<Address> = mutableListOf(),
    @OneToMany(cascade = [CascadeType.PERSIST, CascadeType.REMOVE])
    var cardList: MutableList<Card> = mutableListOf(),
//    @OneToOne(cascade = [CascadeType.PERSIST, CascadeType.REMOVE])
//    private var userCredentials: UserCredentials
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
)