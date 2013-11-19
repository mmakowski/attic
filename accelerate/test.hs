import Prelude hiding (zipWith, fromIntegral)
import Data.Array.Accelerate 

dotp :: Acc (Vector Float) -> Acc (Vector Float) -> Acc (Scalar Float)
dotp xs ys = fold (+) 0 (zipWith (*) xs ys)

v1 :: Acc (Vector Float)
v1 = generate (index1 100000) (\_ -> 1.2)
v2 :: Acc (Vector Float)
v2 = generate (index1 100000) (\ix -> let (Z :. i) = unlift ix in fromIntegral i)

test = dotp v1 v2

-- try in ghci: print test
-- will show that test is an unevaluated Acc operation

-- then: 
-- Data.Array.Accelerate.Interpreter.run test
-- Data.Array.Accelerate.CUDA.run test
