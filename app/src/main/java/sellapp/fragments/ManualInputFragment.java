package sellapp.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import sellapp.models.Address;
import sellapp.models.Order;

import com.example.sellapplingen.R;

import sellapp.models.Recipient;

import com.example.sellapplingen.databinding.FragmentManualInputBinding;

public class ManualInputFragment extends Fragment
    {

    private boolean isEditingAddress = false;


    private FragmentManualInputBinding binding;
    private Order currentOrder;
    private String selectedZipCode = "";
    private String[] zipCodes = {"49808", "49809", "49811"}; // Array der verfügbaren PLZ-Optionen

    public ManualInputFragment()
        {

        }

    public void setCurrentOrder(Order order)
        {
        currentOrder = order;
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
        binding = FragmentManualInputBinding.inflate(inflater, container, false);
        setupViews();

            Bundle args = getArguments();
            if (args != null) {
                currentOrder = (Order) args.getSerializable("order");
                isEditingAddress = args.getBoolean("isEditingAddress", false);

                if (currentOrder != null && currentOrder.getRecipient() != null) {
                    Recipient recipient = currentOrder.getRecipient();
                    binding.firstNameEditText.setText(recipient.getFirstName());
                    binding.lastNameEditText.setText(recipient.getLastName());

                    if (recipient.getAddress() != null) {
                        binding.streetEditText.setText(recipient.getAddress().getStreet());
                        binding.houseNumberEditText.setText(recipient.getAddress().getHouseNumber());
                    }
                }

            }

        return binding.getRoot();


        }



    private void setupViews()
        {
        binding.confirmManualInputButton.setOnClickListener(v -> saveManualInput());

        // Initialisiere den Spinner
        ArrayAdapter<String> zipAdapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_spinner_dropdown_item, zipCodes);
        binding.zipSpinner.setAdapter(zipAdapter);

        binding.zipSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
            @Override
            public void onItemSelected(
                    AdapterView<?> parentView,
                    View selectedItemView,
                    int position,
                    long id
                                      )
                {
                selectedZipCode = zipCodes[position];
                }

            @Override
            public void onNothingSelected(AdapterView<?> parentView)
                {
                }
            });

        binding.houseNumberEditText.addTextChangedListener(new TextWatcher()
            {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
                {
                }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
                {
                if (charSequence.length() > 5)
                    {
                    // Schneiden Sie den Text auf 5 Zeichen ab
                    binding.houseNumberEditText.setText(charSequence.subSequence(0, 5));
                    binding.houseNumberEditText.setSelection(5); // Setzen Sie den Cursor am Ende
                    }
                }

            @Override
            public void afterTextChanged(Editable editable)
                {
                }
            });
        }
    private void saveManualInput()
        {

        if (isEditingAddress)
            {

            Address address = new Address(
                    binding.streetEditText.getText().toString(),
                    binding.houseNumberEditText.getText().toString(), selectedZipCode
            );
            Recipient recipient = new Recipient(
                    binding.firstNameEditText.getText().toString(),
                    binding.lastNameEditText.getText().toString(), address
            );
            currentOrder.setRecipient(recipient);

            isEditingAddress = false;

                navigateToDeliveryDetailsFragment();
            }
        else
            {
            if (isInputValid())
                {
                Address address = new Address(
                        binding.streetEditText.getText().toString(),
                        binding.houseNumberEditText.getText().toString(), selectedZipCode
                );
                Recipient recipient = new Recipient(
                        binding.firstNameEditText.getText().toString(),
                        binding.lastNameEditText.getText().toString(), address
                );
                currentOrder.setRecipient(recipient);

                Bundle args = new Bundle();
                args.putSerializable("order", currentOrder);

                HandlingInfoFragment handlingInfoFragment = new HandlingInfoFragment();
                handlingInfoFragment.setArguments(args);

                FragmentManager fragmentManager = requireFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(
                        R.id.frame_layout, handlingInfoFragment, "handlingInfoFragment");
                transaction.addToBackStack(null);
                transaction.commit();
                }
            else
                {
                    System.out.println(isEditingAddress + "Das ist Manuelinput");
                Toast.makeText(
                             requireContext(), "Bitte füllen Sie alle Felder aus.", Toast.LENGTH_SHORT)
                     .show();
                }
            }
        }


    private boolean isInputValid()
        {
        String lastName = binding.lastNameEditText.getText().toString();
        String firstName = binding.firstNameEditText.getText().toString();
        String street = binding.streetEditText.getText().toString();
        String houseNumber = binding.houseNumberEditText.getText().toString();

        return !lastName.isEmpty() &&
               !firstName.isEmpty() &&
               !street.isEmpty() &&
               !houseNumber.isEmpty() &&
               !selectedZipCode.isEmpty();
        }
        private void navigateToDeliveryDetailsFragment() {

            DeliveryDetailsFragment deliveryDetailsFragment = DeliveryDetailsFragment.newInstance(currentOrder);

            customerapp.models.customerapp.FragmentManagerHelper.replace(
                    requireFragmentManager(), R.id.frame_layout, deliveryDetailsFragment);

            FragmentManager fragmentManager = requireFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.addToBackStack(null);
            transaction.commit();
        }







    }