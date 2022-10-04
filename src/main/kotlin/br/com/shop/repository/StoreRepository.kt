package br.com.shop.repository

import br.com.shop.model.Product
import br.com.shop.model.store.Extract
import br.com.shop.model.store.Store
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.transaction.annotation.Transactional

@RepositoryRestResource(collectionResourceRel = "stores", path = "stores")
interface StoreRepository: PagingAndSortingRepository<Store, Long> {

    fun findByName(nameStore: String, pageable: Pageable): Page<Store>

    @Transactional
    @Query("SELECT s.products FROM Store s WHERE s.id = :id")
    fun findAllProductsByStoreId(@Param("id") id: Long, pageable: Pageable): Page<List<Product>>

    @Transactional
    @Query("SELECT e FROM Store s, Extract e WHERE s.id = :id AND e.id = s.id")
    fun findExtractById(@Param("id") id: Long): Extract

    @Transactional
    @Modifying
    @Query("UPDATE Extract e SET e.balance= e.balance -:value WHERE e.id = " +
            "(SELECT s.extract FROM Store s WHERE s.id= :id)")
    //UPDATE TABELA a JOIN TABLEB b ON a.colA = b.colB SET a.columnToUpdate = [VALOR]
     fun withdraw(@Param("id") id: Long,@Param("value") value: Double)

}