package com.kontrol.api.finance

import com.kontrol.api.auth.CurrentUser
import com.kontrol.api.user.*
import jakarta.validation.Valid
import jakarta.validation.constraints.Positive
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

data class TransactionRequest(val type:String,@field:Positive val amount:Double,val description:String?,val date:LocalDate,val categoryId:Long?,val accountId:Long?)
data class CategoryRequest(val name:String,val type:String="EXPENSE",val icon:String?=null)
data class AccountRequest(val name:String,val type:String="WALLET",val initialBalance:Double=0.0)
data class TransactionResponse(val id:Long,val type:String,val amount:Double,val description:String?,val date:LocalDate,val category:CategoryResponse?,val account:AccountResponse?)
data class CategoryResponse(val id:Long,val name:String,val type:String,val icon:String?)
data class AccountResponse(val id:Long,val name:String,val type:String,val balance:Double)

@RestController @RequestMapping("/api")
class FinanceController(private val current:CurrentUser,private val transactions:TransactionRepository,private val categories:CategoryRepository,private val accounts:FinancialAccountRepository){
 @GetMapping("/transactions") fun transactions():List<TransactionResponse>{val u=current.get();return transactions.findAllByUserIdOrderByDateDescCreatedAtDesc(u.id).map{it.toDto()}}
 @PostMapping("/transactions") fun create(@Valid @RequestBody r:TransactionRequest):TransactionResponse{val u=current.get();val c=r.categoryId?.let{categories.findByIdAndUserId(it,u.id)};val a=r.accountId?.let{accounts.findByIdAndUserId(it,u.id)};return transactions.save(Transaction(user=u,category=c,account=a,type=r.type,amount=r.amount,description=r.description,date=r.date)).toDto()}
 @GetMapping("/categories") fun categories()=categories.findAllByUserId(current.get().id).map{CategoryResponse(it.id,it.name,it.type,it.icon)}
 @PostMapping("/categories") fun createCategory(@RequestBody r:CategoryRequest)=categories.save(Category(user=current.get(),name=r.name,type=r.type,icon=r.icon)).let{CategoryResponse(it.id,it.name,it.type,it.icon)}
 @GetMapping("/financial-accounts") fun accounts()=accounts.findAllByUserId(current.get().id).map{AccountResponse(it.id,it.name,it.type,balance(it))}
 @PostMapping("/financial-accounts") fun createAccount(@RequestBody r:AccountRequest)=accounts.save(FinancialAccount(user=current.get(),name=r.name,type=r.type,initialBalance=r.initialBalance)).let{AccountResponse(it.id,it.name,it.type,balance(it))}
 private fun balance(a:FinancialAccount):Double{val ts=transactions.findAllByUserIdOrderByDateDescCreatedAtDesc(a.user.id).filter{it.account?.id==a.id};return a.initialBalance+ts.sumOf{if(it.type=="INCOME")it.amount else -it.amount}}
 private fun Transaction.toDto()=TransactionResponse(id,type,amount,description,date,category?.let{CategoryResponse(it.id,it.name,it.type,it.icon)},account?.let{AccountResponse(it.id,it.name,it.type,balance(it))})
}
