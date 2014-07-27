-- SVG images

type Point     = (Float, Float)
type Color     = (Int, Int, Int)
type Polygon   = [Point]

gray :: Color
gray = (80, 80, 80)

createPolygons :: [(Color, Polygon)] -> String 
createPolygons p = 
    let createPoint (x, y) = (show x) ++ "," ++ (show y) ++ " "
        createPolygon ((r, g, b), p) = "<polygon points=\"" ++ (concatMap createPoint p) ++ "\" style=\"fill:#cccccc;stroke:rgb(" ++ (show r) ++ "," ++ (show g) ++ "," ++ (show b) ++ ");stroke-width:2\"/>"
    in "<svg xmlns=\"http://www.w3.org/2000/svg\">" ++ (concatMap createPolygon p) ++ "</svg>"

main :: IO ()
main = 
    putStr $ createPolygons [(gray, [(100,100),(200,100),(200,200),(100,200)]), (gray, [(200,200),(300,200),(300,300),(200,300)])]

