package streams;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @see http://winterbe.com/posts/2014/07/31/java8-stream-tutorial-examples/
 * @author Richard Eigenmann
 */
public class Streams {

    /**
     * @param args the command line arguments
     */
    public static void main( String[] args ) {
        new Streams().go();
    }

    private void go() {

        List<String> myList
                = Arrays.asList( "a1", "a2", "b1", "c2", "c1" );

        System.out.println( "Original List:" );
        myList.stream()
                .forEach( s -> System.out.print( s + " " ) );

        System.out.println( "\nFiltered uppercased:" );
        myList
                .stream()
                .filter( s -> s.startsWith( "c" ) )
                .map( String::toUpperCase )
                .sorted()
                .forEach( s -> System.out.print( s + " " ) );

        System.out.println( "\nfindFirst:" );
        myList.stream()
                .findFirst()
                .ifPresent( s -> System.out.print( s + " " ) );

        List<Integer> myIntList = Arrays.asList( 5, 4, 7, 9, 2, 4, 3 );
        System.out.println( "\n----------\nOriginal List:" );
        myIntList.stream()
                .forEach( i -> System.out.print( i + " " ) );

        int sum = myIntList.stream()
                .mapToInt( i -> i )
                .sum();
        System.out.println( "\nSum of entire Array: " + sum );

        sum = myIntList.stream()
                .mapToInt( i -> i )
                .skip( 1 )
                .sum();
        System.out.println( "Sum of Array with skip(1): " + sum );

        System.out.println( "Sliced Array [2..4]:" );
        myIntList
                .subList( 2, 4 )
                .stream()
                .forEach( i -> System.out.print( i + " " ) );

        sum = myIntList
                .subList( 2, 4 )
                .stream()
                .mapToInt( i -> i )
                .sum();
        System.out.println( "\nSum of sliced Array [2..4]: " + sum );

        System.out.println( "----------\nDirect Stream.of:" );
        Stream.of( "a1", "a2", "a3" )
                .forEach( s -> System.out.print( s + " " ) );

        System.out.println( "\n----------\nIntStream.range(1,21):" );
        IntStream.range( 1, 21 )
                .forEach( i -> System.out.print( i + " " ) );

        System.out.println( "\n----------\nIntStream.range(1,6) with concatenation:" );
        IntStream.range( 1, 6 )
                .mapToObj( i -> "Gaga" + i + ' ' )
                .forEach( System.out::print );

        List<Person> persons
                = Arrays.asList(
                        new Person( "Max", 18 ),
                        new Person( "Peter", 23 ),
                        new Person( "Pamela", 23 ),
                        new Person( "David", 12 ) );

        System.out.println( "\n----------\nPersons List:" );
        System.out.println( persons );

        System.out.println( "----------\nFiltered list of Persons:" );
        List<Person> filtered
                = persons
                .stream()
                .filter( p -> p.name.startsWith( "P" ) )
                .collect( Collectors.toList() );
        System.out.println( filtered );    // [Peter, Pamela]

        System.out.println( "----------\nPersons grouped by Age into a map:" );
        Map<Integer, List<Person>> personsByAge = persons
                .stream()
                .collect( Collectors.groupingBy( p -> p.age ) );
        personsByAge
                .forEach( ( age, p ) -> System.out.format( "age %d: %s%n", age, p ) );

        Map<Integer, String> map = persons
                .stream()
                .collect( Collectors.toMap(
                        p -> p.age,
                        p -> p.name,
                        ( name1, name2 ) -> name1 + ";" + name2 ) );

        System.out.println( map );
        // {18=Max, 23=Peter;Pamela, 12=David}

        System.out.println( "----------\nAverage Age:" );
        Double averageAge = persons
                .stream()
                .collect( Collectors.averagingInt( p -> p.age ) );
        System.out.println( averageAge );

        System.out.println( "----------\nIntSummaryStatistics:" );
        IntSummaryStatistics ageSummary
                = persons
                .stream()
                .collect( Collectors.summarizingInt( p -> p.age ) );
        System.out.println( ageSummary );

        System.out.println( "----------\nString Concatenation (joining):" );
        String phrase = persons
                .stream()
                .filter( p -> p.age >= 18 )
                .map( p -> p.name )
                .collect( Collectors.joining( " and ", "In Germany ", " are of legal age." ) );

        System.out.println( phrase );
        // In Germany Max and Peter and Pamela are of legal age.

        String phrase2 = persons
                .stream()
                .filter( p -> p.age >= 18 )
                .map( p -> p.name )
                .collect( Collectors.joining( "\", \"", "(\"", "\")" ) );
        System.out.println( phrase2 );

        System.out.println( "----------\nOur own Collector:" );
        Collector<Person, StringJoiner, String> personNameCollector
                = Collector.of(
                        () -> new StringJoiner( " | " ), // supplier
                        ( j, p ) -> j.add( p.name.toUpperCase() ), // accumulator
                        ( j1, j2 ) -> j1.merge( j2 ), // combiner
                        StringJoiner::toString );                // finisher

        String names = persons
                .stream()
                .collect( personNameCollector );

        System.out.println( names );  // MAX | PETER | PAMELA | DAVID

        System.out.println( "" );

    }

    private class Person {

        String name;
        int age;

        Person( String name, int age ) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return name;
        }
    }

}
