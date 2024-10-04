class Student {
    var name: String? = null
    var age: Int? = null
    var speciality: String? = null
}

var student1 = Student()

// СКОУП ФУНКЦИЯ APPLY
// НАЗНАЧАЕТ ПОЛЯ ОБЪЕКТА И ВОЗВРАЩАЕТ ОБЪЕКТ
var result1 = student1.apply {
    name = "Azizbek"
    age = 18
    speciality = "Digital Engineering"
}
println("Name: ${student1.name}")
println("Result: $result1")


var student2 = Student()

// СКОУП ФУНКЦИЯ RUN
// НАЗНАЧАЕТ ПОЛЯ ОБЪЕКТА И ВОЗВРАЩАЕТ РЕЗУЛЬТАТ ФУНКЦИИ
var result2 = student2.run {
    name = "Shahzoda"
    age = 19
    speciality = "Marketing"
    "My name is $name, the age is $age and my speciality is $speciality"
}
println("Name: ${student2.name}")
println("Result: $result2")


var student3: Student? = Student()

// СКОУП ФУНКЦИЯ LET
// ОПЕРАЦИИ С ОБЪЕКТОМ ЕСЛИ НЕ РАВЕН NULL И ВОЗВРАЩАЕТ РЕЗУЛЬТАТ ФУНКЦИИ
var result3 = student3?.let {
    it.name = "Davron"
    it.age = 20
    it.speciality = "Computer Science"
    "Student name: ${it.name}, age: ${it.age}, speciality: ${it.speciality}"
}
println("Name: ${student3!!.name}")
println("Result: $result3")


// СКОУП ФУНКЦИЯ ALSO
// ВОЗВРАЩАЕТ ОБЪЕКТ
var result4 = student2.also {
    println("Logging student details: Name = ${it.name}, Age = ${it.age}, Speciality = ${it.speciality}")
}
println("Result Object: $result4")


var student5 = Student()

// СКОУП ФУНКЦИЯ WITH
// ВОЗВРАЩАЕТ РЕЗУЛЬТАТ ФУНКЦИИ
var result5 = with(student5) {
    name = "Jamshid"
    age = 22
    speciality = "Physics"
    "Student details: Name = $name, Age = $age, Speciality = $speciality"
}
println("Name: ${student5.name}")
println("Result: $result5")