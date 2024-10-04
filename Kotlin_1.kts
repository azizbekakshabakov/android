class MyInfo constructor(nameParam: String, birthDateParam: String, cityParam: String,
                         courseParam: String, specialityParam: String) {
    var name: String = nameParam
        get() {
            return "Name: $field"
        }
    var birthDate: String = birthDateParam
        get() {
            return "Date of birth: $field"
        }
    var city: String = cityParam
        get() {
            return "City of birth: $field"
        }
    var course: String = courseParam
        get() {
            return "Course level number: $field"
        }
    var speciality: String = specialityParam
        get() {
            return "Speciality: $field"
        }
}

class RelativeInfo constructor(nameParam: String, professionParam: String, ageParam: String) {
    var name: String = nameParam
        get() {
            return "Name: $field"
        }
    var profession: String = professionParam
        get() {
            return "Profession: $field"
        }
    var age: String = ageParam
        get() {
            return "Age: $field"
        }
}

var myInfo = MyInfo("Azizbek", "30/05/2002", "Almaty",
    "4", "Digital Engineering");
var brotherInfo = RelativeInfo("Akbar", "Plumber", "26");
var sisterInfo = RelativeInfo("Pari", "Teacher", "24");

println("My info")
println("Name: ${myInfo.name}")
println("Date of birth: ${myInfo.birthDate}")
println("City of birth: ${myInfo.city}")
println("Course: ${myInfo.course}")
println("Speciality: ${myInfo.speciality}")

println("My brother info")
println("Name: ${brotherInfo.name}")
println("Profession: ${brotherInfo.profession}")
println("Name: ${brotherInfo.name}")

println("My sister info")
println("Name: ${sisterInfo.name}")
println("Profession: ${sisterInfo.profession}")
println("Name: ${sisterInfo.name}")
