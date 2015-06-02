import com.lowworker.domain.GetBeaconsUsecase;
import com.lowworker.domain.GetBeaconsUsecaseController;
import com.lowworker.model.entities.BeaconsWrapper;
import com.lowworker.model.rest.RestDataSource;
import com.squareup.otto.Bus;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by saulmm on 18/02/15.
 */
public class GetBeaconsUsecaseTest {

    private GetBeaconsUsecase getBeaconsUsecase;

    @Mock
    private RestDataSource mockDataSource;

    @Mock
    private Bus mockUiBus;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        getBeaconsUsecase = new GetBeaconsUsecaseController(
                mockDataSource, mockUiBus
        );
    }

    @Test
    public void testMoviesRequestExecution() {

        getBeaconsUsecase.execute();

        verify(mockDataSource, times(1))
            .getBeaconsByPage(1);

    }

    @Test
    public void testUiFilmsPost () {


        // Called from rest
        getBeaconsUsecase.onBeaconsReceived(
                any(BeaconsWrapper.class));

        verify(mockUiBus, times(1)).post(
            any(BeaconsWrapper.class));

    }
}
