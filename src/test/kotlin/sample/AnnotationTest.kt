package sample

import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class AnnotationTest {
    
    @Rule
    @JvmField
    val folder = TemporaryFolder()
    
    @Test
    fun testTemporaryFolder() {
        println(folder.create())
    }
}