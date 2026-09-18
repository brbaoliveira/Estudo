package com.kontrol.api.shopping
import com.kontrol.api.auth.CurrentUser
import com.kontrol.api.user.*
import org.springframework.web.bind.annotation.*

data class ShoppingListRequest(val name:String)
data class ShoppingItemRequest(val name:String,val quantity:String?,val completed:Boolean=false)
data class ShoppingItemResponse(val id:Long,val name:String,val quantity:String?,val completed:Boolean)
data class ShoppingListResponse(val id:Long,val name:String,val items:List<ShoppingItemResponse>)
@RestController @RequestMapping("/api")
class ShoppingController(private val current:CurrentUser,private val lists:ShoppingListRepository,private val items:ShoppingItemRepository){
 @GetMapping("/shopping-lists") fun all()=lists.findAllByUserIdOrderByCreatedAtDesc(current.get().id).map{dto(it)}
 @PostMapping("/shopping-lists") fun create(@RequestBody r:ShoppingListRequest)=dto(lists.save(ShoppingList(user=current.get(),name=r.name)))
 @PostMapping("/shopping-lists/{id}/items") fun add(@PathVariable id:Long,@RequestBody r:ShoppingItemRequest):ShoppingItemResponse{val l=lists.findByIdAndUserId(id,current.get().id)?:throw notFound();return itemDto(items.save(ShoppingItem(list=l,name=r.name,quantity=r.quantity,completed=r.completed)))}
 @PutMapping("/shopping-items/{id}") fun update(@PathVariable id:Long,@RequestBody r:ShoppingItemRequest):ShoppingItemResponse{val x=items.findByIdAndListUserId(id,current.get().id)?:throw notFound();x.name=r.name;x.quantity=r.quantity;x.completed=r.completed;return itemDto(items.save(x))}
 private fun dto(l:ShoppingList)=ShoppingListResponse(l.id,l.name,items.findAllByListId(l.id).map{itemDto(it)})
 private fun itemDto(x:ShoppingItem)=ShoppingItemResponse(x.id,x.name,x.quantity,x.completed)
 private fun notFound()=org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND)
}
