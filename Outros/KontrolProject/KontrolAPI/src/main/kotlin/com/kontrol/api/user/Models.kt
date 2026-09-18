package com.kontrol.api.user

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Entity @Table(name="users")
class User(
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) var id:Long=0,
 @Column(nullable=false) var name:String="",
 @Column(nullable=false,unique=true) var email:String="",
 @Column(nullable=false) var password:String="",
 @Column(nullable=false) var createdAt:LocalDateTime=LocalDateTime.now()
)

@Entity @Table(name="categories")
class Category(
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) var id:Long=0,
 @ManyToOne(optional=false) @JoinColumn(name="user_id") var user:User,
 @Column(nullable=false) var name:String="",
 @Column(nullable=false) var type:String="EXPENSE",
 var icon:String?=null
)

@Entity @Table(name="financial_accounts")
class FinancialAccount(
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) var id:Long=0,
 @ManyToOne(optional=false) @JoinColumn(name="user_id") var user:User,
 @Column(nullable=false) var name:String="",
 @Column(nullable=false) var type:String="WALLET",
 @Column(nullable=false) var initialBalance:Double=0.0
)

@Entity @Table(name="transactions")
class Transaction(
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) var id:Long=0,
 @ManyToOne(optional=false) @JoinColumn(name="user_id") var user:User,
 @ManyToOne @JoinColumn(name="category_id") var category:Category?=null,
 @ManyToOne @JoinColumn(name="account_id") var account:FinancialAccount?=null,
 @Column(nullable=false) var type:String="EXPENSE",
 @Column(nullable=false) var amount:Double=0.0,
 var description:String?=null,
 @Column(nullable=false) var date:LocalDate=LocalDate.now(),
 @Column(nullable=false) var createdAt:LocalDateTime=LocalDateTime.now()
)

@Entity @Table(name="work_schedules")
class WorkSchedule(
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) var id:Long=0,
 @ManyToOne(optional=false) @JoinColumn(name="user_id") var user:User,
 @Column(nullable=false) var name:String="Padrão",
 @Column(nullable=false) var dailyMinutes:Int=480,
 var startTime:LocalTime?=null,
 var endTime:LocalTime?=null
)

@Entity @Table(name="work_records", uniqueConstraints=[UniqueConstraint(columnNames=["user_id","date"])])
class WorkRecord(
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) var id:Long=0,
 @ManyToOne(optional=false) @JoinColumn(name="user_id") var user:User,
 @Column(nullable=false) var date:LocalDate=LocalDate.now(),
 var entryTime:LocalTime?=null,
 var breakStart:LocalTime?=null,
 var breakEnd:LocalTime?=null,
 var exitTime:LocalTime?=null,
 @Column(nullable=false) var workedMinutes:Int=0,
 @Column(nullable=false) var expectedMinutes:Int=480,
 @Column(nullable=false) var createdAt:LocalDateTime=LocalDateTime.now()
)

@Entity @Table(name="notes")
class Note(
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) var id:Long=0,
 @ManyToOne(optional=false) @JoinColumn(name="user_id") var user:User,
 @Column(nullable=false) var title:String="",
 @Lob @Column(nullable=false) var content:String="",
 @Column(nullable=false) var favorite:Boolean=false,
 @Column(nullable=false) var createdAt:LocalDateTime=LocalDateTime.now(),
 @Column(nullable=false) var updatedAt:LocalDateTime=LocalDateTime.now()
)

@Entity @Table(name="shopping_lists")
class ShoppingList(
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) var id:Long=0,
 @ManyToOne(optional=false) @JoinColumn(name="user_id") var user:User,
 @Column(nullable=false) var name:String="",
 @Column(nullable=false) var createdAt:LocalDateTime=LocalDateTime.now()
)

@Entity @Table(name="shopping_items")
class ShoppingItem(
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) var id:Long=0,
 @ManyToOne(optional=false) @JoinColumn(name="shopping_list_id") var list:ShoppingList,
 @Column(nullable=false) var name:String="",
 var quantity:String?=null,
 @Column(nullable=false) var completed:Boolean=false
)

@Entity @Table(name="tasks")
class Task(
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) var id:Long=0,
 @ManyToOne(optional=false) @JoinColumn(name="user_id") var user:User,
 @Column(nullable=false) var title:String="",
 var description:String?=null,
 @Column(nullable=false) var date:LocalDate=LocalDate.now(),
 @Column(nullable=false) var priority:String="NORMAL",
 @Column(nullable=false) var completed:Boolean=false,
 @Column(nullable=false) var createdAt:LocalDateTime=LocalDateTime.now(),
 @Column(nullable=false) var updatedAt:LocalDateTime=LocalDateTime.now()
)
