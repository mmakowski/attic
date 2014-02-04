module hello

-- difficult to specify the requirements using dependent types:

data FB = Number Int | Fizz | Buzz | FizzBuzz

isDivisibleBy : Int -> Int -> Bool
isDivisibleBy n m = (n `mod` m) == 0

tofb : Int -> FB
tofb n = if      n `isDivisibleBy` 15 then FizzBuzz 
         else if n `isDivisibleBy`  5 then Buzz
         else if n `isDivisibleBy`  3 then Fizz
         else                              Number n

fizzbuzz : List FB
fizzbuzz = map tofb [1..100]
