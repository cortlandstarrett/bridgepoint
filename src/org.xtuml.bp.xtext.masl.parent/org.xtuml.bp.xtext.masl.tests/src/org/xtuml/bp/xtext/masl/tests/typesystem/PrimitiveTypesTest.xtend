package org.xtuml.bp.xtext.masl.tests.typesystem

import com.google.inject.Inject
import org.junit.Test
import org.xtuml.bp.xtext.masl.tests.AbstractMaslModelTest
import org.xtuml.bp.xtext.masl.typesystem.MaslTypeProvider

import static org.junit.Assert.*

class PrimitiveTypesTest extends AbstractMaslModelTest {

	@Inject extension MaslTypeProvider

	@Test
	def testEnum() {
		assertPrimitiveType('''
			domain dom is
				service svc();
			 	type bar is enum (BAR, BAZ);
			end;
		''', '''
			service dom::scv(b0: in bar) is
				b1: bar;
			begin
				^b0;
				^b1 := ^BAR;
			end;
		''', 'enum bar')		
	}
	
	@Test
	def testIntegerTypes() {
		for(typeName: #['integer', 'byte', 'long_integer'])
			assertPrimitiveType('''
				domain dom is
					service svc();
					type Bar is «typeName»;
					type Baz is Bar;
				end;
			''', '''
				service dom::scv() is
					b0: Bar;
					b1: Baz;
				begin
					^b0;
					^b1;
					1;
				end;
			''', 'builtin long_integer')		
	}
	
	@Test
	def testOtherBuiltinTypes() {
		for (typeName : #['character', 'string', 'boolean', 'real', 'device',
			'duration', 'timestamp', 'timer'])
			assertPrimitiveType('''
				domain dom is
					service svc();
					type Bar is «typeName»;
					type Baz is Bar;
				end;
			''', '''
				service dom::scv() is
					b0: Bar;
					b1: Baz;
				begin
					^b0;
					^b1;
				end;
			''', 'builtin ' + typeName)		
	}
	
	@Test
	def testObjectType() {
		assertPrimitiveType('''
			domain dom is
				service svc();
				object Foo; 
				object Foo is end;
			end;
		''', '''
			service dom::scv(f0: in instance of Foo) is
				f1: instance of Foo;
			begin
				^f0;
				^f1;
			end;
		''', 'instance of Foo')		
	}
//	
	@Test
	def testCollections() {
		for(collectionType: #['bag', 'set', 'sequence'])
			assertPrimitiveType('''
				domain dom is
					service svc();
					type c is «collectionType» of integer;
				end;
			''', '''
				service dom::scv(c0: in c) is
					c1: c;
				begin
					^c0;
					^c1;
				end;
			''', 'sequence of builtin integer')		
	}

	@Test
	def testArray() {
		assertPrimitiveType('''
			domain dom is
				service svc();
				type c is integer (1..1);
			end;
		''', '''
			service dom::scv(c0: in c) is
				c1: c;
			begin
				^c0;
				^c1;
			end;
		''', 'sequence of builtin integer')		
	}

	@Test
	def testTerminator() {
		assertPrimitiveType('''
			domain dom is
				service svc();
				terminator Arnold is end;
			end;
		''', '''
			service dom::scv() is
			begin
				^Arnold;
			end;
		''', 'terminator Arnold')		
	}
	
	@Test 
	def void testStruct() {
		assertPrimitiveType('''
			domain dom is
				service svc();
				type i is integer;
				type j is i;
				type s is structure
					b: integer := 1;
					c: i;
					d: j;
				end;
			end;
		''', '''
			service dom::scv() is
				x: s;
			begin
				^x;
			end;
		''', '''
			structure
				builtin long_integer;
				builtin long_integer;
				builtin long_integer;
			end
		''')		
	}
	
	protected def assertPrimitiveType(CharSequence modFile, CharSequence extFile, String expectedPrimitiveType) {
		for (expr: getElementsAtCarets('dummy.mod' -> modFile, 'dummy.ext' -> extFile)) 
			assertEquals(expectedPrimitiveType, expr.maslType.primitiveType.toString)
	}
}