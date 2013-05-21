-- this runs pretty nicely with +RTS -N2

import Control.Concurrent.STM (atomically, STM) 
import Control.Concurrent.STM.TQueue (TQueue, newTQueueIO, tryReadTQueue, writeTQueue)
import Control.Concurrent (forkIO) 
import Control.Monad (forever, forM, forM_, when)
import Data.Maybe (catMaybes)

import Graphics.UI.WX hiding (when)

-- Graphics.UI.WX.Async

type UpdateQueue = TQueue (IO ())

data AsyncConfig = AsyncConfig { pollIntervalMs :: Int
                               , batchSize      :: Int
                               }

defaultConfig :: AsyncConfig
defaultConfig = AsyncConfig { pollIntervalMs = 10
                            , batchSize      = 100
                            }

mkUpdateQueue :: Frame a -> IO UpdateQueue
mkUpdateQueue = mkUpdateQueueWithConfig defaultConfig

mkUpdateQueueWithConfig :: AsyncConfig -> Frame a -> IO UpdateQueue
mkUpdateQueueWithConfig cfg f = do
  q <- newTQueueIO
  timer f [ interval   := pollIntervalMs cfg
          , on command := processUiUpdates (batchSize cfg) q
          ]
  return q

processUiUpdates :: Int -> UpdateQueue -> IO ()
processUiUpdates n q = atomically (tryTake n q) >>= sequence_

tryTake :: Int -> TQueue a -> STM [a]
tryTake n q = forM [1..n] (\_ -> tryReadTQueue q) >>= return . catMaybes

postGUIAsync :: UpdateQueue -> IO a -> IO ()
postGUIAsync q u = atomically $ writeTQueue q $ do u; return ()

------------

main :: IO ()
main = start $ do
  f <- frame [ text := "Test" ]
  l <- staticText f [ text := "starting" ]
  q <- mkUpdateQueue f
  forkIO $ findPrimes q l

findPrimes :: UpdateQueue -> StaticText () -> IO ()
findPrimes q l = forM_ (filter isPrime [2000..]) $ \n -> 
  postGUIAsync q $ set l [ text := show n ]

isPrime :: Integer -> Bool
isPrime n = isPrime' n 2
  where 
    isPrime' n d = 
      if (fromIntegral d) > sqrt (fromIntegral n) then True
      else if n `mod` d == 0 then False
           else isPrime' n (d+1)