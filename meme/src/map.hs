import Data.Graph

type Point = (Float, Float)
type Item = (Vertex, Point)
type Drawing = [Item]

inAndOutEdges :: Vertex -> Graph -> [Edge]
inAndOutEdges vertex = (filter (\(from, to) -> from == vertex || to == vertex)) . edges

otherEnd :: Vertex -> Edge -> Maybe Vertex
otherEnd c (a, b) = if c == a then Just b
                    else if c == b then Just a
                    else Nothing

scale = 200

drawMap :: Graph -> Vertex -> Drawing
drawMap graph first = 
    let centre = (0, 0)
        extract (Just a) = [a]
        extract Nothing = []
        neighbours = concatMap extract $ map (otherEnd first) $ inAndOutEdges first graph
        range = 2 * pi
        step = range / (fromInteger $ toInteger $ length neighbours)
    in (first, centre) : drawNeighbours centre (scale, 0) step neighbours

drawNeighbours :: Point -> Point -> Float -> [Vertex] -> Drawing
drawNeighbours _ _ _ [] = []
drawNeighbours centre current step (v : vs) = 
    (v, current) : drawNeighbours centre (nextPoint centre current step) step vs

nextPoint :: Point -> Point -> Float -> Point
nextPoint (x0, y0) (x1, y1) step = 
    let (x1', y1') = (x1 - x0, y1 - y0)
        ts = tan step
        g = (x1' - ts * y1') / (ts * x1' + y1')
        nang = (atan2 y1' x1') + step
        y2sig = if nang < 0 then -1 else if nang > pi then -1 else 1
        y2' = sqrt ((x1' ^ 2 + y1' ^ 2) / (g ^ 2 + 1)) * y2sig 
        x2' = g * y2'
    in (x2' + x0, y2' + y0)

showGraph g n = 
    let key (_, t, _) = t
    in concatMap (key . n) (vertices g)

drawingToSvg :: (Vertex -> (node, String, [String])) -> Graph -> Drawing -> String
drawingToSvg n graph drawing = 
    "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"20cm\" height=\"20cm\" viewBox=\"0 0 " ++ (show (4 * scale)) ++ " " ++ (show (4 * scale)) ++"\"><g transform=\"translate(" ++ (show $ scale * 2) ++ "," ++ (show $ scale * 2) ++ ")\">" ++ (concatMap (itemToSvg n) drawing) ++ (concatMap (edgeToSvg drawing) (edges graph)) ++ "</g></svg>"

itemToSvg :: (Vertex -> (node, String, [String])) -> Item -> String
itemToSvg n (vertex, (x, y)) = 
    let (_, label, _) = n vertex
    in "<text x=\"" ++ (show $ x) ++ "\" y=\"" ++ (show $ y) ++ "\" font-size=\"12\" font-family=\"Verdana\">" ++ label ++ "</text>"

edgeToSvg :: Drawing -> Edge -> String
edgeToSvg d e = ""

{-
buildGraph :: IO (Graph, Vertex -> (node, String, [String]), String -> Maybe Vertex, String)
buildGraph = 
    let lines =
-}  

getLines :: [(Integer, String)] -> Integer -> IO [(Integer, String)]
getLines lines last = do
  line <- getLine
  if (line == "")
    then do return $ reverse lines
    else do
        allLines <- getLines ((last + 1, line) : lines) (last + 1)
        return allLines

split :: String -> Char -> [String]
split [] delim = [""]
split (c:cs) delim
   | c == delim = "" : rest
   | otherwise = (c : head rest) : tail rest
   where
       rest = split cs delim

strip :: String -> String
strip s = dropWhile ws $ reverse $ dropWhile ws $ reverse s
    where ws = (`elem` [' ', '\n', '\t', '\r'])

toNeighbourList :: (Integer, String) -> (Integer, String, [String])
toNeighbourList (n, t) =
    let
        (elem : nStr) = split t ':'
        neighbours = split (head nStr) ','
        neighbours' = if neighbours == [""] then [] else neighbours
    in (n, strip elem, map strip neighbours')

buildGraph :: IO (Graph, Vertex -> (Integer, String, [String]), String -> Maybe Vertex, String)
buildGraph = do
  let 
      result edges@((_, label, _) : _) = 
          let (g, n, v) = graphFromEdges edges
          in (g, n, v, label)
  lines <- getLines [] 0
  return $ result $ map toNeighbourList lines


main :: IO ()
main = 
    let extract (Just a) = a
    in do
      {-putStr $ showGraph g n
      putStr $ show $ inAndOutEdges 1 g
      putStr "\n"-}
      (g, n, v, first) <- buildGraph
      putStr $ (drawingToSvg n g) $ drawMap g $ extract $ v first --"transactional memory"
