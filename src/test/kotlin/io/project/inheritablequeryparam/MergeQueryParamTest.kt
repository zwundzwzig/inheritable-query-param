package io.project.inheritablequeryparam

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class MergeQueryParamTest {

    private val main = MergeQueryParam()

    @Test
    @DisplayName("하나가_Null인_경우")
    fun testNullToString() {
        Assertions.assertEquals("division1=상의", main.mergeQueryParam("division1=상의", null, false))
        Assertions.assertEquals("division1=상의", main.mergeQueryParam("division1=상의", null, true))
        Assertions.assertEquals("division1=상의", main.mergeQueryParam(null, "division1=상의", true))
        Assertions.assertEquals("", main.mergeQueryParam(null, "division1=상의", false))
    }

    @Test
    @DisplayName("Null 없이 겹치는 Key도 없는 경우")
    fun testNonNullAndNonDuplicateKey() {
        Assertions.assertEquals("division1=상의&keywords=반팔티&ids=1,2", main.mergeQueryParam("keywords=반팔티&ids=1,2", "division1=상의", true))
    }

    @Test
    @DisplayName("상속여부 false")
    fun testInheritableFalse() {
        Assertions.assertEquals("division1=상의&keywords=반팔티&ids=1,2", main.mergeQueryParam("division1=상의&&keywords=반팔티&ids=1,2", "division1=하의", false))
        Assertions.assertEquals("keywords=반팔티&ids=1,2", main.mergeQueryParam("keywords=반팔티&ids=1,2", "division1=상의", false))
    }

    @Test
    @DisplayName("상위_쿼리_파라미터의_값_우선_적용")
    fun testDuplicateKeyInInheritance() {
        Assertions.assertEquals("division1=상의&keywords=반팔티&ids=1,2", main.mergeQueryParam("division1=상의&&keywords=반팔티&ids=1,2", "division1=상의", true))
        Assertions.assertEquals("division1=하의,상의&keywords=반팔티&ids=1,2", main.mergeQueryParam("division1=상의&&keywords=반팔티&ids=1,2", "division1=하의", true))
        Assertions.assertEquals("division1=하의,상의&keywords=나시,반팔티&ids=1,2", main.mergeQueryParam("division1=상의&&keywords=반팔티&ids=1,2", "division1=하의&keywords=나시&ids=1,2", true))
        Assertions.assertEquals("division1=하의,상의&keywords=나시,반팔티&ids=1,2", main.mergeQueryParam("division1=상의&&keywords=반팔티&ids=1,2", "division1=하의&keywords=나시&ids=1", true))
        Assertions.assertEquals("division1=하의,상의&keywords=나시,반팔티&ids=1,3,2", main.mergeQueryParam("division1=상의&&keywords=반팔티&ids=1,2", "division1=하의&keywords=나시&ids=1,3", true))
    }

    @Test
    @DisplayName("동일 레벨의 쿼리 파라미터에 동일 key 존재 시 선행 값 우선 적용")
    fun testDuplicateKeyInNonInheritance() {
        Assertions.assertEquals("division1=상의&keywords=반팔티&ids=1,2", main.mergeQueryParam("division1=상의&division1=하의&keywords=반팔티&ids=1,2", "division1=하의", false))
        Assertions.assertEquals("division1=상의&keywords=반팔티&ids=1,2", main.mergeQueryParam("division1=상의&division1=하의,신발&keywords=반팔티&ids=1,2", "division1=하의", false))
        Assertions.assertEquals("division1=상의,모자&keywords=반팔티&ids=1,2", main.mergeQueryParam("division1=모자&&keywords=반팔티&ids=1,2", "division1=상의&division1=하의", true))
    }

}