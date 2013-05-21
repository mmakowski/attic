import scala.language.implicitConversions // requires scala 2.10

import scala.math.abs
import scala.collection.immutable.{ Stack, Vector }
import scala.collection.mutable.StringBuilder

object Chessboard {
  def main(args: Array[String]) = {
    val (boardSize, pieces, quiet) = parse(args)
    val solutionCount = solutions(boardSize, stacked(pieces), quiet)
    println("found %d solutions" format (solutionCount))
  }

  // the top level of the search tree, executes in parallel
  def solutions(size: Position, pieces: Stack[Piece], quiet: Boolean): Int =
    if (pieces.isEmpty) 1
    else {
      val board = Board(size)
      board.emptyPositions().par.map { pos => 
        solutions(board.withPiece(pieces.top, pos), pieces.pop, pieces.top, pos, quiet)
      }.sum
    }

  def solutions(maybeBoard: Option[Board], 
                pieces:     Stack[Piece], 
                lastPiece:  Piece, 
                lastPos:    Position,
                quiet:      Boolean): Int = maybeBoard match {
    case None        => 0
    case Some(board) =>
      if (pieces.isEmpty) {
        if (!quiet) println(board)
        1
      } else {
        // if current piece is the same as the last piece placed, only attempt to place it
        // on positions higher than that of the previous piece -- this will eliminate duplicates
        val fromPos = if (lastPiece == pieces.top) lastPos else Position(0, 0)
        board.emptyPositions(fromPos).foldLeft(0) { (count, pos) => 
          count + solutions(board.withPiece(pieces.top, pos), pieces.pop, pieces.top, pos, quiet)
        }
      }
    }

  case class Position(x: Int, y: Int) {
    def +(other: Position) = Position(x + other.x, y + other.y)
    def -(other: Position) = Position(x - other.x, y - other.y)
  }

  implicit def intPairToPosition(p: (Int, Int)) = Position(p._1, p._2)

  abstract class Piece(symbol: String) {
    def attackedPositions(board: Board, piecePosition: Position): Seq[Position]
    override def toString() = symbol
  }

  case object King extends Piece("K") {
    def attackedPositions(board: Board, piecePosition: Position) = 
      for (dx <- -1 to 1; dy <- -1 to 1;
           pos = piecePosition + (dx, dy)
           if board.contains(pos)) 
        yield pos
  }

  case object Queen extends Piece("Q") {
    def attackedPositions(board: Board, piecePosition: Position) =
      board.rowAndColumnThrough(piecePosition) ++ 
      board.diagonalsThrough(piecePosition)
  }

  case object Bishop extends Piece("B") {
    def attackedPositions(board: Board, piecePosition: Position) = 
      board.diagonalsThrough(piecePosition)
  }

  case object Rook extends Piece("R") {
    def attackedPositions(board: Board, piecePosition: Position) = 
      board.rowAndColumnThrough(piecePosition)
  }

  case object Knight extends Piece("N") {
    def attackedPositions(board: Board, piecePosition: Position) = 
      for (dx <- -2 to 2; dy <- -2 to 2;
           pos = piecePosition + (dx, dy)
           if abs(dx) != abs(dy) && dx != 0 && dy != 0 && board.contains(pos))
        yield pos
  }

  val piecesOrderedByEliminationPower = Seq(Queen, Rook, Bishop, King, Knight)

  sealed trait Field
  case object Empty                 extends Field { override def toString() = "." }
  case object Attacked              extends Field { override def toString() = "." }
  case class Occupied(piece: Piece) extends Field { override def toString() = piece.toString }

  class Board private (val         size:   Position, 
                       private val fields: Seq[Field]) {
    def withPiece(piece: Piece, position: Position): Option[Board] = {      
      val attacks = piece.attackedPositions(this, position)
      if (anyContainsPiece(attacks)) None
      else {
        val fieldsWithNewAttacks = attacks.foldLeft(fields) { (fields, pos) => 
          fields.updated(fieldOffset(pos), Attacked)
        }
        Some(new Board(size, fieldsWithNewAttacks.updated(fieldOffset(position), Occupied(piece))))
      }
    }

    private def anyContainsPiece(positions: Seq[Position]) = 
      positions.foldLeft(false) { (result, position) => result || isOccupied(position) }

    def isOccupied(position: Position) = fieldAt(position) match {
      case Occupied(_) => true
      case _           => false
    }

    def fieldAt(position: Position) = fields(fieldOffset(position))

    def emptyPositions(from: Position = (0, 0)): Seq[Position] =
      for (x <- 0 until size.x; y <- 0 until size.y;
           pos = Position(x, y)
           if isAfter(from, pos) && fieldAt(pos) == Empty) 
        yield pos

    private def isAfter(before: Position, after: Position): Boolean = 
      after.x == before.x && after.y >= before.y || after.x > before.x

    def contains(position: Position): Boolean = 
      position.x >= 0 && position.x < size.x &&
      position.y >= 0 && position.y < size.y

    def rowAndColumnThrough(pos: Position) = row(pos.y) ++ column(pos.x)

    def column(x: Int): Seq[Position] = for (y <- 0 until size.y) yield Position(x, y)

    def row(y: Int): Seq[Position] = for (x <- 0 until size.x) yield Position(x, y)

    def diagonalsThrough(pos: Position) = 
      for (x <- 0 until size.x; y <- 0 until size.y 
           if x - y == pos.x - pos.y || x + y == pos.x + pos.y)
        yield Position(x, y)

    private def fieldOffset(position: Position): Int = position.y * size.x + position.x

    override def toString(): String = {
      val buffer = new StringBuilder()
      for (y <- 0 until size.y) {
        for (x <- 0 until size.x) {
          buffer.append(fieldAt((x, y)).toString)
        }
        buffer.append("\n")
      }
      buffer.toString
    }
  }

  object Board extends Function1[Position, Board] {
    def apply(size: Position) = new Board(size, emptyFields(size))

    private def emptyFields(size: Position): Seq[Field] = Vector.fill(size.x * size.y)(Empty)
  }

  def stacked(counts: Map[Piece, Int]) = 
    piecesOrderedByEliminationPower.foldLeft(Stack[Piece]()) { (stack, piece) => 
      stack.pushAll(Seq.fill(counts(piece))(piece))
    }

  def parse(args: Array[String]): (Position, Map[Piece, Int], Boolean) = {
    if (args.length < 7 || args.length > 8) showUsageAndExit()
    val quiet =  args.length == 8 && args(7) == "--quiet"
    try {
      ((int(args(0)), int(args(1))), 
       Map[Piece, Int](King   -> int(args(2)),
                       Queen  -> int(args(3)),
                       Bishop -> int(args(4)),
                       Rook   -> int(args(5)),
                       Knight -> int(args(6))),
       quiet)
    } catch {
      case _: NumberFormatException => showUsageAndExit()
    }
  }

  def int(str: String): Int = Integer.parseInt(str)

  def showUsageAndExit(): Nothing = {
    val usage = """usage: chessboard WIDTH HEIGHT KINGS QUEENS BISHOPS ROOKS KNIGHTS [--quiet]"""
    System.err.println(usage)
    sys.exit(2)
  }
}
