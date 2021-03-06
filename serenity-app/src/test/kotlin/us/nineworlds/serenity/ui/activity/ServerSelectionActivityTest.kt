package us.nineworlds.serenity.ui.activity

import android.widget.TextView
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.android.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.quality.Strictness.STRICT_STUBS
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import us.nineworlds.serenity.R
import us.nineworlds.serenity.TestingModule
import us.nineworlds.serenity.common.Server
import us.nineworlds.serenity.test.InjectingTest
import us.nineworlds.serenity.ui.activity.login.LoginUserActivity
import java.util.*

@RunWith(RobolectricTestRunner::class)
class ServerSelectionActivityTest : InjectingTest() {

  @Rule
  @JvmField
  val rule = MockitoJUnit.rule().strictness(STRICT_STUBS)

  @Mock
  lateinit var mockServer: Server

  lateinit var activity: ServerSelectionActivity

  @Before
  override fun setUp() {
    Robolectric.getBackgroundThreadScheduler().pause()
    Robolectric.getForegroundThreadScheduler().pause()
    activity = Robolectric.buildActivity(ServerSelectionActivity::class.java).create().visible().get()
  }

  @Test
  fun serverDelayHandlerIsInitialized() {
    assertThat(activity.serverDisplayHandler).isNotNull
  }

  @Test
  fun createServerListHidesProgressBarAndDisplaysServerContainer() {
    Robolectric.flushForegroundThreadScheduler()
    Robolectric.flushBackgroundThreadScheduler()

    Assertions.assertThat(activity.serverLoadingContainer).isGone
    Assertions.assertThat(activity.serverContainer).isVisible
  }

  @Test
  fun displayHandlerPopulatesServerContainer() {
    val expectedId = UUID.randomUUID().toString()
    activity.servers[expectedId] = mockServer

    doReturn("Emby").whenever(mockServer).discoveryProtocol()
    doReturn("test").whenever(mockServer).serverName

    Robolectric.flushBackgroundThreadScheduler()
    Robolectric.flushForegroundThreadScheduler()

    Assertions.assertThat(activity.serverContainer).hasChildCount(3)
  }

  @Test
  fun refreshButtonIsOnlyItemWhenServerListIsEmpty() {
    Robolectric.flushForegroundThreadScheduler()
    Robolectric.flushForegroundThreadScheduler()

    assertThat(activity.servers).isEmpty()
    Assertions.assertThat(activity.serverContainer).hasChildCount(2);
  }

  @Test
  fun serverInfoAddedToServerContainerSetsExpectedTextOnServerTextView() {
    val expectedId = UUID.randomUUID().toString()
    activity.servers[expectedId] = mockServer

    doReturn("Emby").whenever(mockServer).discoveryProtocol()
    doReturn("test").whenever(mockServer).serverName

    Robolectric.flushBackgroundThreadScheduler()
    Robolectric.flushForegroundThreadScheduler()

    val view = activity.serverContainer.getChildAt(1)
    val textView = view.findViewById<TextView>(R.id.server_name)

    Assertions.assertThat(textView).hasText("test")
  }

  @Test
  fun clickingOnServerOptionContainerStartsExpectedActivity() {
    val expectedId = UUID.randomUUID().toString()
    activity.servers[expectedId] = mockServer

    doReturn("Emby").whenever(mockServer).discoveryProtocol()
    doReturn("test").whenever(mockServer).serverName
    doReturn("testserver").whenever(mockServer).ipAddress

    Robolectric.flushBackgroundThreadScheduler()
    Robolectric.flushForegroundThreadScheduler()

    val view = activity.serverContainer.getChildAt(1)
    view.performClick()

    val shadowActivity = shadowOf(activity)
    Assertions.assertThat(shadowActivity.nextStartedActivity).hasComponent(activity, LoginUserActivity::class.java)
  }

  override fun installTestModules() {
    scope.installTestModules(TestingModule())
  }
}