/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 */

import { NewAppScreen } from '@react-native/new-app-screen';
import {
  Button,
  StatusBar,
  StyleSheet,
  ScrollView,
  Text,
  useColorScheme,
  View,
} from 'react-native';
import {
  SafeAreaProvider,
  useSafeAreaInsets,
} from 'react-native-safe-area-context';
import Container from './components/Container';
import CalendarModule from './modules/Test'

function App() {
  const isDarkMode = useColorScheme() === 'dark';

  return (
    <SafeAreaProvider>
      <StatusBar barStyle={isDarkMode ? 'light-content' : 'dark-content'} />
      <AppContent />
    </SafeAreaProvider>
  );
}

function AppContent() {
  const safeAreaInsets = useSafeAreaInsets();
  const isDarkMode = useColorScheme() === 'dark';
  let elms = [];
  for (let i = 0; i <= 43; i++) {
    elms.push(
      <Container key={i} bgColor="#09f">
        <Text key={i}>Element {i}</Text>
      </Container>,
    );
  }
  const onPress = () => {
    console.log("Doing work");
    CalendarModule.createCalendarEvent('foo', 'bar');
  }
  return (
    <View
      style={{
        backgroundColor: isDarkMode ? '#f3f3f3' : '#f3f3f3',
        paddingTop: safeAreaInsets.top,
        paddingLeft: safeAreaInsets.left,
        paddingRight: safeAreaInsets.right,
      }}
    >
      <Text>Hi this is a new app and this stays on top!</Text>
      <Button
            title="Click to invoke your native module!"
            color="#841584"
            onPress={onPress}
          />
      <ScrollView
        style={{
          marginBottom: safeAreaInsets.bottom,
        }}
      >
        <View
          style={{
            alignContent: 'center',
            alignItems: 'center',
            width: '100%',
          }}
        >
          {elms}
          <Text>Hi this is a new app </Text>
          {/* Give some extra padding at the bottom */}
          <Text style={{ height: 40 }}></Text>
        </View>
        <NewAppScreen></NewAppScreen>
      </ScrollView>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
});

export default App;
