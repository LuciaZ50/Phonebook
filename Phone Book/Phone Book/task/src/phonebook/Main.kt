package phonebook



import java.io.File
import kotlin.math.sqrt

fun main() {

    val directory = File("C:/Users/Lucia/Downloads/directory.txt").readLines()
    val find = File("C:/Users/Lucia/Downloads/find.txt").readLines()

    linear(directory, find)
    bubbleAndJump(directory, find)
    quickSortAndBinarySearch(directory, find)
}
fun name(line: String): String {
    return line.substringAfter(" ")
}

fun linear(name: String, list: List<String>): Boolean{
    for(entry in list){
        if(name(entry) == name){
            return true
        }
    }
    return false
}

fun linear(directory: List<String>, find: List<String>){
    println("Start searching (linear search)...")


    val start = System.currentTimeMillis()
    var found = 0

    for(name in find){
        if (linear(name, directory)){
            found++
        }
    }
    val end = System.currentTimeMillis()
    val time = end - start

    println("Found $found /500 entries. Time taken ${taken(end-start)}")
    println()
}


fun bubble(list: List<String>):List<String> {
    val start = System.currentTimeMillis()
    val mutableList = list.toMutableList()
    do{
        var switched = false
        for (i in 0 until mutableList.size - 1) {
            if (name(mutableList[i]) > name(mutableList[i + 1])) {
                switched = true
                val temp = mutableList[i]
                mutableList[i] = mutableList[i + 1]
                mutableList[i + 1] = temp
            }
        }
    }while (switched)
    val end = System.currentTimeMillis()
    val time = end - start

    println("Sorting time: ${taken(end-start)}")

    return mutableList.toList()
}

fun jump(name: String, list: List<String>):Boolean{
    val interval = sqrt(list.size.toDouble()).toInt()

    for(index in 0..list.size step interval){
        val entry = list[index]

        if(name(entry) == name){
            return true
        }else if (name(entry) < name){
            continue
        }else {
            var j = index
            while (name(list[j]) > name && j>=0){
                j--
            }
            return true
        }
    }
    return false
}

fun internal(list: List<String>): List<String> {
    return list.sortedBy { name(it) }
}

fun bubbleAndJump(directory: List<String>, find: List<String>){
    println("Start searching (bubble sort + jump search)...")

    val start1 = System.currentTimeMillis()
    val sorted = internal(directory)
    val end1 = System.currentTimeMillis()

    val start = System.currentTimeMillis()
    var found = 0
    for(name in find){
        if(jump(name, sorted)){
            found++
        }
    }
    val end = System.currentTimeMillis()
    val time = end - start
    val time1 = end1 - start1
    val time2 = end - start1

    println("Found $found / 500 entries. Time taken ${taken(end -start1)}")
    println("Sorting time: ${taken(end1-start1)}")
    println("Searching time: ${taken(end-start)}")
    println()
}

fun taken(duration: Long): String {
    return String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", duration)
}

fun division(list: MutableList<String>, start3: Int, end3: Int): Int {
    var start = start3
    var end = end3

    while (start < end) {
        while (start < end) {
            if (name(list[start]) > name(list[end])) {
                val swap = list[start]
                list[start] = list[end]
                list[end] = swap
                break
            }
            end -= 1
        }
        while (start < end) {
            if (name(list[start]) > name(list[end])) {
                val swap = list[start]
                list[start] = list[end]
                list[end] = swap
                break
            }
            start += 1
        }
    }
    return start
}

fun quicksort(list: MutableList<String>, _start: Int = -1, _end: Int = -1) {
    var start = _start
    var end = _end

    if (start == -1) start = 0
    if (end == -1) end = list.size

    if (start < end) {
        val i = division(list, start, end - 1)
        quicksort(list, start, i)
        quicksort(list, i + 1, end)
    }
}


fun quickSortAndBinarySearch(directory: List<String>, wantedNames: List<String>) {
    println("Start searching (quick sort + binary search)...")

    val startTotal = System.currentTimeMillis()
    val mutDir = directory.toMutableList()
    quicksort(mutDir)
    val sortedDirectory = mutDir

    val endSort = System.currentTimeMillis()

    var found = 0
    for (name in wantedNames) {
        if (binarySearch(name, sortedDirectory)) {
            found++
        }
    }
    val end = System.currentTimeMillis()

    println("Found $found / 500 entries. Time taken: ${taken(end - startTotal)}")
    println("Sorting time: ${taken(endSort - startTotal)}")
    println("Searching time:: ${taken(end - endSort)}")

    println()
}

fun binarySearch(name: String, list: List<String>): Boolean {
    if (list.isEmpty()) {
        return false
    } else if (list.size == 1) {
        return name(list[0]) == name
    }

    val middle = list.size / 2

    if (name(list[middle]) == name) {
        return true
    } else if (name(list[middle]) < name) {
        return binarySearch(name, list.subList(middle + 1, list.size))
    } else {
        return binarySearch(name, list.subList(0, middle))
    }
}

fun hashTable(directory: List<String>, wantedNames: List<String>) {
    println("Start searching (hash table)...")

    val startTotal = System.currentTimeMillis()
    val map: HashMap<String, String> = HashMap()
    for (entry in directory) {
        map[name(entry)] = phone(entry)
    }
    val endCreateHashMap = System.currentTimeMillis()

    var found = 0
    for (wanted in wantedNames) {
        if (map.containsKey(wanted)) {
            found++
        }
    }

    val end = System.currentTimeMillis()

    println("Found $found / 500 entries. Time taken: ${taken(end - startTotal)}")
    println("Creating time: ${taken(endCreateHashMap - startTotal)}")
    println("Searching time:: ${taken(end - endCreateHashMap)}")

    println()
}